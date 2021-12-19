package com.olexiy.rewardModule.services;

import java.util.ArrayList;
import java.util.List;

import com.olexiy.rewardModule.models.DTO.AttractionDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import gpsUtil.location.Attraction;

@Service
public class GpsServiceWEB {
    
    @Autowired
    @Qualifier("GpsWebClient")
    private WebClient webClient; // See implementation in RewardModuleConfig.

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
