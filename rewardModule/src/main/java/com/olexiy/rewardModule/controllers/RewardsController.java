package com.olexiy.rewardModule.controllers;

import java.util.List;
import com.olexiy.rewardModule.models.User;
import com.olexiy.rewardModule.models.DTO.LocationDTO;
import com.olexiy.rewardModule.models.DTO.UserDTO;
import com.olexiy.rewardModule.services.RewardsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gpsUtil.location.Location;

@RestController
public class RewardsController {

    @Autowired
    private RewardsService rewardsService;

    @GetMapping(value = "/rewardsApp")
    public ResponseEntity<String> hello() {
        return new ResponseEntity<>("RewardsApp", HttpStatus.OK);
    }

    @PostMapping(value = "/calculateRewards")
    public List<UserDTO> calculateRewards(@RequestBody List<UserDTO> users) {
        List<User> allUsers = rewardsService.convertUserDTOtoUser(users);
        List<User> usersWithRewards = rewardsService.calculateRewardsMultiThreading(allUsers);
        return rewardsService.convertUserToUserDTO(usersWithRewards);
    }
    
    @GetMapping(value = "/setProximityBuffer")
    public int setProximityBuffer(@RequestParam(name = "value") Integer value) {
        return rewardsService.setProximityBuffer(value);
    }

    @PostMapping(value = "/getDistance")
    public Double getDistance(@RequestBody List<LocationDTO> locations) {
        // Converting Entity to DTO in order to be able to send it vie http and deserialize
        Location loc1 = new Location(locations.get(0).getLatitude(), locations.get(0).getLongitude());
        Location loc2 = new Location(locations.get(1).getLatitude(), locations.get(1).getLongitude());
        return rewardsService.getDistance(loc1, loc2);
    }
}
