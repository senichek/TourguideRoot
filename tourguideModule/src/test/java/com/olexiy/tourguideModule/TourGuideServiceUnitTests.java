package com.olexiy.tourguideModule;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.olexiy.tourguideModule.helper.InternalTestHelper;
import com.olexiy.tourguideModule.models.User;
import com.olexiy.tourguideModule.models.UserReward;
import com.olexiy.tourguideModule.services.TourGuideService;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.MethodMode;
import org.springframework.test.context.ActiveProfiles;

import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
public class TourGuideServiceUnitTests {

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
	public void addUser() {    	
		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		User user2 = new User(UUID.randomUUID(), "jon2", "000", "jon2@tourGuide.com");

		tourGuideService.addUser(user);
		tourGuideService.addUser(user2);
		
		User retrivedUser = tourGuideService.getUser(user.getUserName());
		User retrivedUser2 = tourGuideService.getUser(user2.getUserName());
		
		assertEquals(user, retrivedUser);
		assertEquals(user2, retrivedUser2);
	}

	@Test
	public void getUserRewardsTest() {
		Attraction attraction = new Attraction("Attr", "city", "state", 42.06323582113245, -97.37893290938405);
		Location location = new Location(42.710839313025254, -94.68366891086124);
		VisitedLocation visitedLocation = new VisitedLocation(UUID.randomUUID(), location, new Date());
		UserReward reward = new UserReward(visitedLocation, attraction);
		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		user.addUserReward(reward);
		assertEquals(1, tourGuideService.getUserRewards(user).size());
	}

	@Test
	public void getUserLocationTest() {
		Location location = new Location(42.710839313025254, -94.68366891086124);
		VisitedLocation visitedLocation = new VisitedLocation(UUID.randomUUID(), location, new Date());
		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		user.addToVisitedLocations(visitedLocation);
		assertEquals(visitedLocation, tourGuideService.getUserLocation(user));
	}

	@Test
	public void copyRewardsBetweenUserCollectionsTest() {
		UUID userID = UUID.randomUUID();
		Attraction attraction = new Attraction("Attr2", "city2", "state2", 42.16323582113245, -97.47893290938405);
		Location location = new Location(40.710839313025254, -90.68366891086124);
		VisitedLocation visitedLocation = new VisitedLocation(UUID.randomUUID(), location, new Date());
		UserReward reward = new UserReward(visitedLocation, attraction);
		User user = new User(userID, "jon1", "0001", "jon1@tourGuide.com");
		User userWithReward = new User(userID, "jon1", "0001", "jon1@tourGuide.com");
		userWithReward.addUserReward(reward);

		tourGuideService.addUser(user);
		List<User> usersWithRewards = new ArrayList<>();
		usersWithRewards.add(userWithReward);
		// 3 users with reward were added in the previous test, so the result must be 4.
		// The tests should be run all at once because they share the collection of users.
		assertEquals(4, tourGuideService.copyRewardsBetweenUserCollections(usersWithRewards).size());
	}
}