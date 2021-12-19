package com.olexiy.rewardCentralModule.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import rewardCentral.RewardCentral;


@RestController
public class RewardCentralController {

    @Autowired
    private RewardCentral rewardCentral;

    @PostMapping(value = "/getAttractionRewardPoints")
    public int getAttractionRewardPoints(@RequestBody List<UUID> data) {
        UUID attractionId = data.get(0);
        UUID userId = data.get(1);
        return rewardCentral.getAttractionRewardPoints(attractionId, userId);
    }
}
