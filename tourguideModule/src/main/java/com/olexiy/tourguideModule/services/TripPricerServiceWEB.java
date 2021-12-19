package com.olexiy.tourguideModule.services;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.olexiy.tourguideModule.models.DTO.ProviderDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import tripPricer.Provider;

@Service
public class TripPricerServiceWEB {

    @Autowired
    @Qualifier("TripPricerWebClient")
    private WebClient webClient; // See implementation in TourguideModuleConfig.

    public List<Provider> getPrice(String tripPricerApiKey, UUID userId, int numOfAdults, int numOfKids,
            int tripDuration, int cumulatativeRewardPoints) {
        List<Object> data = new ArrayList<>();
        data.add(tripPricerApiKey);
        data.add(userId);
        data.add(numOfAdults);
        data.add(numOfKids);
        data.add(tripDuration);
        data.add(cumulatativeRewardPoints);

        List<ProviderDTO> response = webClient.post()
                .uri("/getPrice")
                .body(BodyInserters.fromObject(data))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<ProviderDTO>>() {
                })
                .block();

        // Converting DTOs to Entities.
        List<Provider> result = new ArrayList<>();
        response.forEach(p -> {
            result.add(new Provider(p.getTripId(), p.getName(), p.getPrice()));
        });

        return result;
    }
}