package com.olexiy.tourguideModule.services;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.olexiy.tourguideModule.helper.InternalTestHelper;
import com.olexiy.tourguideModule.helper.Util;
import com.olexiy.tourguideModule.models.User;
import com.olexiy.tourguideModule.models.DTO.UserDTO;
import com.olexiy.tourguideModule.models.UserReward;
import com.olexiy.tourguideModule.tracker.Tracker;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import tripPricer.Provider;

@Service
public class TourGuideService {
	private Logger logger = LoggerFactory.getLogger(TourGuideService.class);
	private final RewardsServiceWEB rewardsServiceWEB;
	private final GpsServiceWEB gpsServiceWEB;
	private final TripPricerServiceWEB tripPricerServiceWEB;
	public final Tracker tracker;
	boolean testMode = true;

	ExecutorService executorService = Executors.newFixedThreadPool(Util.calculateAmountofThreads());

	public TourGuideService(RewardsServiceWEB rewardsServiceWEB, GpsServiceWEB gpsServiceWEB, TripPricerServiceWEB tripPricerServiceWEB) {
		this.rewardsServiceWEB = rewardsServiceWEB;
		this.gpsServiceWEB = gpsServiceWEB;
		this.tripPricerServiceWEB = tripPricerServiceWEB;

		if (testMode) {
			logger.info("TestMode enabled");
			logger.debug("Initializing users");
			initializeInternalUsers();
			logger.debug("Finished initializing users");
		}
	
		tracker = new Tracker(this, rewardsServiceWEB);
		addShutDownHook();
	}

	public List<UserReward> getUserRewards(User user) {
		return user.getUserRewards();
	}

	public VisitedLocation getUserLocation(User user) {
		VisitedLocation visitedLocation = (user.getVisitedLocations().size() > 0) ? user.getLastVisitedLocation()
				: trackUserLocation(user);
		return visitedLocation;
	}

	public User getUser(String userName) {
		return internalUserMap.get(userName);
	}

	public List<User> getAllUsers() {
		return internalUserMap.values().stream().collect(Collectors.toList());
	}

	public List<User> convertUserDTOtoUser(List<UserDTO> users) {
		List<User> result = new ArrayList<>();
		users.forEach(u -> {
			result.add(new User(u));
		});
		return result;
	}

	public List<UserDTO> convertUserToUserDTO(List<User> users) {
		List<UserDTO> result = new ArrayList<>();
		users.forEach(u -> {
			result.add(new UserDTO(u));
		});
		return result;
	}

	public List<User> copyRewardsBetweenUserCollections(List<User> usersWithRewards) {
		List<User> allUsers = getAllUsers();
				usersWithRewards.forEach(userWithRew -> {
					if (userWithRew.getUserRewards().size() != 0 && userWithRew.getUserRewards() != null) {
						allUsers.forEach(u -> {
							if (u.getUserId().equals(userWithRew.getUserId())) {
								userWithRew.getUserRewards().forEach(rew -> {
									if (u.getUserRewards().stream().filter(r -> r.attraction.attractionName.equals(rew.attraction.attractionName)).count() == 0) {
										// Making sure that the reward for visiting a specific attraction is not present in the collection
										// because user can have only one reward per atrraction.
										u.addUserReward(rew);
										logger.debug("Copied reward from " + userWithRew.getUserName() + " to " + u.getUserName());
									}
								});
							}
						});
					}
				});
		return getAllUsers();
	}

	public void addUser(User user) {
		if (!internalUserMap.containsKey(user.getUserName())) {
			internalUserMap.put(user.getUserName(), user);
		}
	}

	public List<Provider> getTripDeals(User user) {
		int cumulatativeRewardPoints = user.getUserRewards().stream().mapToInt(i -> i.getRewardPoints()).sum();
		List<Provider> providers = tripPricerServiceWEB.getPrice(tripPricerApiKey, user.getUserId(),
				user.getUserPreferences().getNumberOfAdults(),
				user.getUserPreferences().getNumberOfChildren(), user.getUserPreferences().getTripDuration(),
				cumulatativeRewardPoints);
		user.setTripDeals(providers);
		return providers;
	}

	public VisitedLocation trackUserLocation(User user) {
		logger.debug("<<trackUserLocation>> was called for " + user.getUserName());
		VisitedLocation visitedLocation = gpsServiceWEB.getUserLocation(user.getUserId());
		user.addToVisitedLocations(visitedLocation);
		return visitedLocation;
	}

	public void trackUserLocationMultiThreading(List<User> users) {
		StopWatch stopWatch = new StopWatch();
		logger.debug("STARTED tracking the users' locations.");
		stopWatch.start();
		List<Callable<VisitedLocation>> tasks = new ArrayList<>();
		users.forEach(u -> {
			tasks.add(new Callable<VisitedLocation>() {
				@Override
				public VisitedLocation call() throws Exception {
					return trackUserLocation(u);
				}
			});
		});
		try {
			executorService.invokeAll(tasks);
		} catch (InterruptedException e) {
			logger.debug("<<executorService.invokeAll>> was interrupted");
			e.printStackTrace();
		}
		stopWatch.stop();
		logger.debug("FINISHED tracking the users' locations. " + "Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds.");
		stopWatch.reset();
	}

	public Map<Double, Attraction> getNearByAttractions(VisitedLocation visitedLocation) {
		Map<Double, Attraction> fiveClosest = new HashMap<>();
		for (Attraction attraction : gpsServiceWEB.getAttractions()) {
			fiveClosest.put(rewardsServiceWEB.getDistance(visitedLocation.location, attraction), attraction);
		}

		Map<Double, Attraction> sorted = fiveClosest.entrySet().stream()
				.sorted(Map.Entry.comparingByKey())
				.limit(5)
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
						(oldValue, newValue) -> oldValue, LinkedHashMap::new));

		return sorted;
	}

	private void addShutDownHook() {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				tracker.stopTracking();
			}
		});
	}
	
	/**********************************************************************************
	 * 
	 * Methods Below: For Internal Testing
	 * 
	 **********************************************************************************/
	private static final String tripPricerApiKey = "test-server-api-key";
	// Database connection will be used for external users, but for testing purposes
	// internal users are provided and stored in memory
	private final Map<String, User> internalUserMap = new ConcurrentHashMap<>();

	private void initializeInternalUsers() {
		IntStream.range(0, InternalTestHelper.getInternalUserNumber()).forEach(i -> {
			String userName = "internalUser" + i;
			String phone = "000";
			String email = userName + "@tourGuide.com";
			User user = new User(UUID.randomUUID(), userName, phone, email);
			generateUserLocationHistory(user);

			internalUserMap.put(userName, user);
		});
		logger.debug("Created " + InternalTestHelper.getInternalUserNumber() + " internal test users.");
	}

	private void generateUserLocationHistory(User user) {
		IntStream.range(0, 3).forEach(i -> {
			user.addToVisitedLocations(new VisitedLocation(user.getUserId(),
					new Location(generateRandomLatitude(), generateRandomLongitude()), getRandomTime()));
		});
	}

	private double generateRandomLongitude() {
		double leftLimit = -180;
		double rightLimit = 180;
		return leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
	}

	private double generateRandomLatitude() {
		double leftLimit = -85.05112878;
		double rightLimit = 85.05112878;
		return leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
	}

	private Date getRandomTime() {
		LocalDateTime localDateTime = LocalDateTime.now().minusDays(new Random().nextInt(30));
		return Date.from(localDateTime.toInstant(ZoneOffset.UTC));
	}
}
