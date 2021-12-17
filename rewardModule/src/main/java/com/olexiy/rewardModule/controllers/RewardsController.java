package com.olexiy.rewardModule.controllers;

import java.util.List;
import java.util.UUID;

import com.olexiy.rewardModule.models.User;
import com.olexiy.rewardModule.models.UserDTO;
import com.olexiy.rewardModule.services.RewardsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@RestController
public class RewardsController {

    @Autowired
    private RewardsService rewardsService;

    @Autowired
    private WebClient webClient;

    /* @GetMapping(value = "/calculateRewards")
    public ResponseEntity<String> calculateRewards(@RequestBody List<User> users) {
        // calculateRewardsMultiThreading returns void
        rewardsService.calculateRewardsMultiThreading(users);
        return new ResponseEntity<>(HttpStatus.OK);
    } */

    @GetMapping(value = "/rewardsApp")
    public ResponseEntity<String> hello() {
        return new ResponseEntity<>("RewardsApp", HttpStatus.OK);
    }

    @PostMapping(value = "/calculateRewards")
    public UserDTO rewards(@RequestBody List<UserDTO> users) {
        User user = new User(UUID.randomUUID(), "TestUser", "555", "212365");
        UserDTO dto = new UserDTO(user);
        return dto;
    }
}
