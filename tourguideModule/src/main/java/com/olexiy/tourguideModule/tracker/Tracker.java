package com.olexiy.tourguideModule.tracker;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.olexiy.tourguideModule.helper.Util;
import com.olexiy.tourguideModule.models.User;
import com.olexiy.tourguideModule.services.RewardsService;
import com.olexiy.tourguideModule.services.TourGuideService;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import reactor.core.publisher.Mono;

public class Tracker extends Thread {
	private Logger logger = LoggerFactory.getLogger(Tracker.class);
	private static final long trackingPollingInterval = TimeUnit.MINUTES.toSeconds(5);
	// My changes
	// Executors.newSingleThreadExecutor was replaced by Executors.newFixedThreadPool(+ method that calculates the number of Threads we may need);
	//private final ExecutorService executorService = Executors.newFixedThreadPool(Util.calculateAmountofThreads());
	private final ExecutorService executorService = Executors.newSingleThreadExecutor();
	private final TourGuideService tourGuideService;
	private final RewardsService rewardsService;
	private boolean stop = false;

	public Tracker(TourGuideService tourGuideService, RewardsService rewardsService) {
		this.tourGuideService = tourGuideService;
		this.rewardsService = rewardsService;

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
			// Removed users.forEach(u -> tourGuideService.trackUserLocation(u));
			// and added lines 58 and 59.
			tourGuideService.trackUserLocationMultiThreading(users);
			rewardsService.calculateRewardsMultiThreading(users);
			rewardsService.testRequest(tourGuideService.getAllUsersDTO(users));
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
