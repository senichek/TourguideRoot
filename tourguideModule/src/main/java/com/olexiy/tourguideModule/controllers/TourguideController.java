package com.olexiy.tourguideModule.controllers;

import java.util.List;
import java.util.UUID;

import javax.print.attribute.standard.Media;

import com.olexiy.tourguideModule.models.User;
import com.olexiy.tourguideModule.models.UserReward;
import com.olexiy.tourguideModule.services.TourGuideService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@RestController
public class TourguideController {

    @Autowired
    private WebClient webClient; // See declaration in TourguideConfig class.

    @Autowired
    private TourGuideService tourGuideService;

    @GetMapping(value = "/tourguideApp")
    public ResponseEntity<String> hello() {
        return new ResponseEntity<>("TourguideApp", HttpStatus.OK);
    }

    @GetMapping(value = "/response")
    public Mono<String> getResponse() {
        Mono<String> bodyToMono = webClient.get()
        .uri("/rewardsApp")
        .retrieve()
        .bodyToMono(String.class);
        return bodyToMono;
    }

    @PostMapping(value = "/response")
    public Mono<String> postResponse() {
        Mono<String> bodyToMono = webClient.post()
        .uri("/calculateRewards")
        .retrieve()
        .bodyToMono(String.class);
        return bodyToMono;
    }

    @PostMapping(value = "/calculateRewards", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getRewards() {
        User user = new User(UUID.randomUUID(), "TestUser", "555", "email@doc.com");
        Mono<User> bodyToMono = webClient.post()
        .uri("/calculateRewards")
        .body(Mono.just(user), User.class)
        .retrieve()
        .bodyToMono(User.class);
        
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/users")
    public ResponseEntity<List<User>> getUsers() {
        return new ResponseEntity<>(tourGuideService.getAllUsers(), HttpStatus.OK);
    }
}
