package com.olexiy.rewardModule.controllers;

import java.util.List;

import com.olexiy.rewardModule.models.User;
import com.olexiy.rewardModule.models.DTO.UserDTO;
import com.olexiy.rewardModule.services.RewardsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RewardsController {

    @Autowired
    private RewardsService rewardsService;

    @GetMapping(value = "/rewardsApp")
    public ResponseEntity<String> hello() {
        return new ResponseEntity<>("RewardsApp", HttpStatus.OK);
    }

    @PostMapping(value = "/calculateRewards")
    public List<UserDTO> rewards(@RequestBody List<UserDTO> users) {
        List<User> allUsers = rewardsService.convertUserDTOtoUser(users);
        List<User> usersWithRewards = rewardsService.calculateRewardsMultiThreading(allUsers);
        return rewardsService.convertUserToUserDTO(usersWithRewards);
    }
}
