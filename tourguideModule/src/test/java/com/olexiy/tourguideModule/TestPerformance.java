package com.olexiy.tourguideModule;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.TimeUnit;

import com.olexiy.tourguideModule.helper.InternalTestHelper;
import com.olexiy.tourguideModule.services.RewardsServiceWEB;
import com.olexiy.tourguideModule.services.TourGuideService;

import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class TestPerformance {
    // Before running the tests you need to start other modules
    // so that the can establish the connection.

    @BeforeAll
	private static void setUp() {
		InternalTestHelper.setTestProfile(true);
		InternalTestHelper.setInternalUserNumber(100000);
	}

    @Autowired
    private TourGuideService tourGuideService;

    @Autowired
    private RewardsServiceWEB rewardsServiceWEB;

    @Test
    public void highVolumeTrackLocation() {
        StopWatch stopWatch = new StopWatch();
		stopWatch.start();
        tourGuideService.trackUserLocationMultiThreading(tourGuideService.getAllUsers());
        stopWatch.stop();
		System.out.println("highVolumeTrackLocation: Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds."); 
        stopWatch.reset();
		assertTrue(TimeUnit.MINUTES.toSeconds(15) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
    }

    @Test
    public void highVolumeGetRewardsTest() {
        StopWatch stopWatch = new StopWatch();
		stopWatch.start();
        rewardsServiceWEB.calculateRewards(tourGuideService.convertUserToUserDTO(tourGuideService.getAllUsers()));
        stopWatch.stop();
		System.out.println("highVolumeGetRewardsTest: Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds."); 
        stopWatch.reset();
		assertTrue(TimeUnit.MINUTES.toSeconds(20) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
    }
}