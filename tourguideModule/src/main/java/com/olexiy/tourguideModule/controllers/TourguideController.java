package com.olexiy.tourguideModule.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.olexiy.tourguideModule.models.User;
import com.olexiy.tourguideModule.models.UserReward;
import com.olexiy.tourguideModule.models.DTO.NearbyAttractionDTO;
import com.olexiy.tourguideModule.services.RewardsServiceWEB;
import com.olexiy.tourguideModule.services.TourGuideService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import rewardCentral.RewardCentral;

@RestController
public class TourguideController {

    @Autowired
    private TourGuideService tourGuideService;

    @Autowired
    private RewardsServiceWEB rewardsServiceWEB;

    // TODO RewardCentral будет отдельным приложением
    @Autowired
	private RewardCentral rewardCentral;

    @RequestMapping("/")
    public String index() {
        return "Greetings from TourGuide!";
    }

    @RequestMapping("/getRewards") 
    public List<UserReward> getRewards(@RequestParam String userName) {
    	return tourGuideService.getUserRewards(getUser(userName));
    }

    @GetMapping(value = "/users")
    public ResponseEntity<List<User>> getUsers() {
        return new ResponseEntity<>(tourGuideService.getAllUsers(), HttpStatus.OK);
    }

    @GetMapping(value = "/setProximityBuffer")
    public int setProximityBuffer(@RequestParam(name = "value") Integer value) {
        return rewardsServiceWEB.setProximityBuffer(value);
    }

    @RequestMapping("/getNearbyAttractions") 
    public List<NearbyAttractionDTO> getNearbyAttractions(@RequestParam String userName) {
    	VisitedLocation currentLocation = tourGuideService.getUserLocation(getUser(userName));
		
		// Returning 5 closest attractions.
		Map<Double, Attraction> fiveClosestLocations = tourGuideService.getNearByAttractions(currentLocation);
		List<NearbyAttractionDTO> result = new ArrayList<>();

		// For each of 5 locations we create DTO.
		fiveClosestLocations.forEach((k, v) -> {
        NearbyAttractionDTO nearbyAttractionDTO = new NearbyAttractionDTO();
		nearbyAttractionDTO.setAttractionName(v.attractionName);
		nearbyAttractionDTO.setAttractionLocation(new Location(v.latitude, v.longitude));
		nearbyAttractionDTO.setUserLocation(currentLocation.location);
		nearbyAttractionDTO.setDistanceInMiles(k); // Distance in kilometers acts as the KEY in the map of fiveClosestLocations.
		nearbyAttractionDTO.setRewardsPoints(rewardCentral.getAttractionRewardPoints(v.attractionId, tourGuideService.getUser(userName).getUserId()));
		result.add(nearbyAttractionDTO);
		});
	   return result;
    }

    private User getUser(String userName) {
    	return tourGuideService.getUser(userName);
    }
}
