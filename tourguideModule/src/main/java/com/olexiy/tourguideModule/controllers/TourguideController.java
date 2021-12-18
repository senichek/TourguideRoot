package com.olexiy.tourguideModule.controllers;

import java.util.List;
import com.olexiy.tourguideModule.models.User;
import com.olexiy.tourguideModule.models.UserReward;
import com.olexiy.tourguideModule.services.TourGuideService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TourguideController {

    @Autowired
    private TourGuideService tourGuideService;

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

    private User getUser(String userName) {
    	return tourGuideService.getUser(userName);
    }
}
