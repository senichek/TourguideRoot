package com.olexiy.tourguideModule.tracker;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.olexiy.tourguideModule.helper.Util;
import com.olexiy.tourguideModule.models.User;
import com.olexiy.tourguideModule.models.DTO.UserDTO;
import com.olexiy.tourguideModule.services.RewardsServiceWEB;
import com.olexiy.tourguideModule.services.TourGuideService;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Tracker extends Thread {
	private Logger logger = LoggerFactory.getLogger(Tracker.class);
	private static final long trackingPollingInterval = TimeUnit.MINUTES.toSeconds(5);
	private final ExecutorService executorService = Executors.newSingleThreadExecutor();
	private final TourGuideService tourGuideService;
	private final RewardsServiceWEB rewardsServiceWEB;
	private boolean stop = false;

	public Tracker(TourGuideService tourGuideService, RewardsServiceWEB rewardsServiceWEB) {
		this.tourGuideService = tourGuideService;
		this.rewardsServiceWEB = rewardsServiceWEB;

		Util.calculateAmountofThreads();
		
		executorService.submit(this);
	}
	
	/**
	 * Assures to shut down the Tracker thread
	 */
	public void stopTracking() {
		stop = true;
		executorService.shutdownNow();
	}
	
	@Override
	public void run() {
		StopWatch stopWatch = new StopWatch();
		while(true) {
			if(Thread.currentThread().isInterrupted() || stop) {
				logger.debug("Tracker stopping");
				break;
			}
			
			List<User> users = tourGuideService.getAllUsers();
			logger.debug("Begin Tracker. Tracking " + users.size() + " users.");
			stopWatch.start();
			tourGuideService.trackUserLocationMultiThreading(users);
			// Sending the users' list (the list of Entities is converted to the list of DTOs) 
			// to Rewards Module to calculate rewards.
			List<UserDTO> usersDTOWithRewards = rewardsServiceWEB.calculateRewards(tourGuideService.convertUserToUserDTO(users));
			// Converting the response (we receive back the list of userDTOs with rewards) back to Entity
			List<User> usersWithRewards = tourGuideService.convertUserDTOtoUser(usersDTOWithRewards);
			// Setting the rewards that we received via WEB to the users in Tourguide Module collection.
			tourGuideService.copyRewardsBetweenUserCollections(usersWithRewards);
			stopWatch.stop();
			logger.debug("Tracker Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds."); 
			stopWatch.reset();
			try {
				logger.debug("Tracker sleeping");
				TimeUnit.SECONDS.sleep(trackingPollingInterval);
			} catch (InterruptedException e) {
				break;
			}
		}
	}
}
