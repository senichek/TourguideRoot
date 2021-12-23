package com.olexiy.tourguideModule;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import com.olexiy.tourguideModule.helper.InternalTestHelper;
import com.olexiy.tourguideModule.models.DTO.UserDTO;
import com.olexiy.tourguideModule.services.RewardsServiceWEB;
import com.olexiy.tourguideModule.services.TourGuideService;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


@SpringBootTest
@ActiveProfiles("test")
public class RewardsServiceWEBTest {

    @Autowired
    private RewardsServiceWEB rewardsServiceWEB;

    @Autowired
    private TourGuideService tourGuideService;
    
    @BeforeAll
	private static void setUp() {
		InternalTestHelper.setTestProfile(true);
		InternalTestHelper.setInternalUserNumber(100);
	}

    @Test
    public void calculateRewardsTest() {
        List<UserDTO> usersWithRewards = rewardsServiceWEB.calculateRewards(tourGuideService.convertUserToUserDTO(tourGuideService.getAllUsers()));
        assertNotNull(usersWithRewards);
    }

    @Test
    public void setProximityBufferTest() {
        assertEquals(25, rewardsServiceWEB.setProximityBuffer(25));
    }
}
