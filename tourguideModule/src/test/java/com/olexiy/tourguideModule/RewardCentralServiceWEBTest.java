package com.olexiy.tourguideModule;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Map;

import com.olexiy.tourguideModule.helper.InternalTestHelper;
import com.olexiy.tourguideModule.models.User;
import com.olexiy.tourguideModule.services.RewardCentralServiceWEB;
import com.olexiy.tourguideModule.services.TourGuideService;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import gpsUtil.location.Attraction;

@SpringBootTest
@ActiveProfiles("test")
public class RewardCentralServiceWEBTest {

    @Autowired
    private RewardCentralServiceWEB rewardCentralServiceWEB;

    @Autowired
    private TourGuideService tourGuideService;

    @BeforeAll
	private static void setUp() {
		InternalTestHelper.setTestProfile(true);
		InternalTestHelper.setInternalUserNumber(1);
	}

    @Test
    public void getAttractionRewardPointsTest() {
        User user = tourGuideService.getAllUsers().get(0);
        Map<Double, Attraction> nearByAttractions = tourGuideService.getNearByAttractions(user.getLastVisitedLocation());
        Map.Entry<Double, Attraction> entry = nearByAttractions.entrySet().iterator().next();
        Attraction attraction = entry.getValue();
        int attractionRewardPoints = rewardCentralServiceWEB.getAttractionRewardPoints(attraction.attractionId, user.getUserId());
        assertNotNull(attractionRewardPoints);
    }
}
