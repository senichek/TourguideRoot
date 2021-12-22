package com.olexiy.tourguideModule;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.olexiy.tourguideModule.helper.InternalTestHelper;
import com.olexiy.tourguideModule.models.User;
import com.olexiy.tourguideModule.services.TourGuideService;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import tripPricer.Provider;

@SpringBootTest
@ActiveProfiles("test")
public class TourGuideServiceIntegrationTests {

    @Autowired
    private TourGuideService tourGuideService;

    // Before running the tests you need to start other modules
    // so that the can establish the connection.
    @BeforeAll
	private static void setUp() {
		InternalTestHelper.setTestProfile(true);
		InternalTestHelper.setInternalUserNumber(0);
	}

    @Test
	public void getUserLocation() {
		InternalTestHelper.setInternalUserNumber(0);
		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		VisitedLocation visitedLocation = tourGuideService.trackUserLocation(user);
		assertTrue(visitedLocation.userId.equals(user.getUserId()));
	}

    @Test
	public void trackUser() {
		InternalTestHelper.setInternalUserNumber(0);
		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		VisitedLocation visitedLocation = tourGuideService.trackUserLocation(user);
		assertEquals(user.getUserId(), visitedLocation.userId);
	}

    @Test
	public void getNearbyAttractions() {
		InternalTestHelper.setInternalUserNumber(0);
		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		VisitedLocation visitedLocation = tourGuideService.trackUserLocation(user);
		// Getting the list of values out of the map of 5 closest locations.
		List<Attraction> attractions = tourGuideService.getNearByAttractions(visitedLocation).values().stream().collect(Collectors.toList());
		assertEquals(5, attractions.size());
	}

    @Test
	public void getTripDeals() {
		InternalTestHelper.setInternalUserNumber(0);
		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		List<Provider> providers = tourGuideService.getTripDeals(user);
		assertEquals(5, providers.size());
	}
}