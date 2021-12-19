package com.olexiy.tourguideModule.services;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.olexiy.tourguideModule.models.DTO.AttractionDTO;
import com.olexiy.tourguideModule.models.DTO.VisitedLocationDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import gpsUtil.location.Location;
import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;

@Service
public class GpsServiceWEB {
    
    @Autowired
    @Qualifier("GpsWebClient")
    private WebClient webClient; // See implementation in TourguideModuleConfig.

    public VisitedLocation getUserLocation(UUID userId) {
        VisitedLocationDTO response = webClient.get()
        .uri(uriBuilder -> uriBuilder
        .path("/getUserLocation")
        .queryParam("userId", userId)
        .build())
        .retrieve()
        .bodyToMono(VisitedLocationDTO.class)
        .block();

        Location location = new Location(response.getLocationDTO().getLatitude(), response.getLocationDTO().getLongitude());

        return new VisitedLocation(response.getUserId(), location, response.getTimeVisited());
    }

    public List<Attraction> getAttractions() {
        List<AttractionDTO> response = webClient.get()
        .uri(uriBuilder -> uriBuilder
        .path("/getAttractions")
        .build())
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<List<AttractionDTO>>() {
        })
        .block();

        List<Attraction> attractions = new ArrayList<>();
        // Converting DTOs to Entity.
        response.forEach(attr -> {
            attractions.add(new Attraction(attr.getAttractionName(), attr.getCity(), attr.getState(), attr.getLatitude(), attr.getLongitude()));
        });

        return attractions;
    }
}