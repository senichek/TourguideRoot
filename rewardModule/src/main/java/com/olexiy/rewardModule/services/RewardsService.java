package com.olexiy.rewardModule.services;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.olexiy.rewardModule.helper.Util;
import com.olexiy.rewardModule.models.User;
import com.olexiy.rewardModule.models.UserReward;
import com.olexiy.rewardModule.models.DTO.UserDTO;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;

@Service
public class RewardsService {
    private static final double STATUTE_MILES_PER_NAUTICAL_MILE = 1.15077945;

	private Logger logger = LoggerFactory.getLogger(RewardsService.class);

	ExecutorService executorService = Executors.newFixedThreadPool(Util.calculateAmountofThreads());

	// proximity in miles
    private int defaultProximityBuffer = 10;
	private int proximityBuffer = defaultProximityBuffer;
	private int attractionProximityRange = 200;
	private final GpsServiceWEB gpsServiceWEB;
	private final RewardCentralServiceWEB rewardCentralServiceWEB;
	
	public RewardsService(GpsServiceWEB gpsServiceWEB, RewardCentralServiceWEB rewardCentralServiceWEB) {
		this.gpsServiceWEB = gpsServiceWEB;
		this.rewardCentralServiceWEB = rewardCentralServiceWEB;
	}
	
	public int setProximityBuffer(int proximityBuffer) {
		this.proximityBuffer = proximityBuffer;
		return this.proximityBuffer;
	}
	
	public void setDefaultProximityBuffer() {
		proximityBuffer = defaultProximityBuffer;
	}
	
	public void calculateRewards(User user) {
		logger.debug("<<calculateRewards>> was called for " + user.getUserName());
		List<VisitedLocation> userLocations = user.getVisitedLocations();
		List<Attraction> attractions = gpsServiceWEB.getAttractions();
		for(VisitedLocation visitedLocation : userLocations) {
			for(Attraction attraction : attractions) {
				if(user.getUserRewards().stream().filter(r -> r.attraction.attractionName.equals(attraction.attractionName)).count() == 0) {
					if(nearAttraction(visitedLocation, attraction)) {
						user.addUserReward(new UserReward(visitedLocation, attraction, getRewardPoints(attraction, user)));
						logger.debug("Reward was created for " + user.getUserName());
					}
				}
			}
		}
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

	public List<User> calculateRewardsMultiThreading(List<User> users) {
		StopWatch stopWatch = new StopWatch();
		logger.debug("STARTED calculating rewards (Rewards Module).");
		stopWatch.start();
		List<Callable<Void>> tasks = new ArrayList<>();
		users.forEach(u -> {
			tasks.add(new Callable<Void>() {
				@Override
				public Void call() throws Exception {
					calculateRewards(u);
					return null;
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
		logger.debug("FINISHED calculating rewards (Rewards Module). " + "Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds.");
		stopWatch.reset();
		return users;
	}

	public boolean isWithinAttractionProximity(Attraction attraction, Location location) {
		return getDistance(attraction, location) > attractionProximityRange ? false : true;
	}
	
	private boolean nearAttraction(VisitedLocation visitedLocation, Attraction attraction) {
		return getDistance(attraction, visitedLocation.location) > proximityBuffer ? false : true;
	}
	
	private int getRewardPoints(Attraction attraction, User user) {
		return rewardCentralServiceWEB.getAttractionRewardPoints(attraction.attractionId, user.getUserId());
	}
	
	public double getDistance(Location loc1, Location loc2) {
        double lat1 = Math.toRadians(loc1.latitude);
        double lon1 = Math.toRadians(loc1.longitude);
        double lat2 = Math.toRadians(loc2.latitude);
        double lon2 = Math.toRadians(loc2.longitude);

        double angle = Math.acos(Math.sin(lat1) * Math.sin(lat2)
                               + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon1 - lon2));

        double nauticalMiles = 60 * Math.toDegrees(angle);
        double statuteMiles = STATUTE_MILES_PER_NAUTICAL_MILE * nauticalMiles;
        return statuteMiles;
	}
}
