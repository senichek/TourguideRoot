package com.olexiy.rewardModule.models;

import java.util.UUID;

import gpsUtil.location.Attraction;

public class AttractionDTO {
    private String attractionName;
    private String city;
    private String state;
    private UUID attractionId;

    public AttractionDTO() {}

    public AttractionDTO(Attraction attraction) {
        this.attractionName = attraction.attractionName;
        this.city = attraction.city;
        this.state = attraction.state;
        this.attractionId = attraction.attractionId;
    }
}