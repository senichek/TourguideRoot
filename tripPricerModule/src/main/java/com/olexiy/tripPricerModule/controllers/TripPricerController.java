package com.olexiy.tripPricerModule.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.olexiy.tripPricerModule.DTO.ProviderDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import tripPricer.Provider;
import tripPricer.TripPricer;

@RestController
public class TripPricerController {

    private final TripPricer tripPricer = new TripPricer();

    @PostMapping(value = "/getPrice")
    public List<ProviderDTO> getPrice(@RequestBody List<String> data) {
        String tripPricerApiKey = data.get(0);
        UUID userId = UUID.fromString(data.get(1));
        int numOfAdults = Integer.parseInt(data.get(2));
        int numOfKids = Integer.parseInt(data.get(3));
        int tripDuration = Integer.parseInt(data.get(4));
        int cumulatativeRewardPoints = Integer.parseInt(data.get(5));

        List<Provider> providers = tripPricer.getPrice(tripPricerApiKey, userId,
        numOfAdults, numOfKids, tripDuration, cumulatativeRewardPoints);

        // Converting Entities to DTOs.
        List<ProviderDTO> providerDTOs = new ArrayList<>();
        providers.forEach(p -> {
            providerDTOs.add(new ProviderDTO(p));
        });

        return providerDTOs;
    }
}
