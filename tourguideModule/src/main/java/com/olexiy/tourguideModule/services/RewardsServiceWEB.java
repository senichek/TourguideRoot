package com.olexiy.tourguideModule.services;

import java.util.ArrayList;
import java.util.List;

import com.olexiy.tourguideModule.models.DTO.LocationDTO;
import com.olexiy.tourguideModule.models.DTO.UserDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import gpsUtil.location.Location;

@Service
public class RewardsServiceWEB {

    @Autowired
    private WebClient webClient; // See implementation in TourguideModuleConfig.

    public List<UserDTO> calculateRewards(List<UserDTO> users) {
        List<UserDTO> response = webClient.post()
                .uri("/calculateRewards")
                .body(BodyInserters.fromObject(users))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<UserDTO>>() {
                })
                .block();

        return response;
    }

    public int setProximityBuffer(Integer proximityBuffer) {
        int response = webClient.get()
            .uri(uriBuilder -> uriBuilder
            .path("/setProximityBuffer")
            .queryParam("value", proximityBuffer)
            .build())
            .retrieve()
            .bodyToMono(Integer.class)
            .block();

        return response;
    }

    public double getDistance(Location loc1, Location loc2) {
        List<LocationDTO> locations = new ArrayList<>();
        // Converting Entity to DTO in order to be able to send it vie http and deserialize it.
        locations.add(new LocationDTO(loc1));
        locations.add(new LocationDTO(loc2));

        Double response = webClient.post()
                .uri("/getDistance")
                .body(BodyInserters.fromObject(locations))
                .retrieve()
                .bodyToMono(Double.class)
                .block();

        return response;
    }
}
