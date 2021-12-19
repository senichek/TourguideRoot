package com.olexiy.rewardModule.services;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class RewardCentralServiceWEB {

    @Autowired
    @Qualifier("RewardCentralWebClient")
    private WebClient webClient; // See implementation in RewardsModuleConfig.

    public int getAttractionRewardPoints(UUID attractionId, UUID userId) {
        List<UUID> data = new ArrayList<>();
        data.add(0, attractionId);
        data.add(1, userId);

        Integer response = webClient.post()
                .uri("/getAttractionRewardPoints")
                .body(BodyInserters.fromObject(data))
                .retrieve()
                .bodyToMono(Integer.class)
                .block();
        return response;
    }
}
