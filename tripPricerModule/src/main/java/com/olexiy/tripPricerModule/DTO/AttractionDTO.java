package com.olexiy.tripPricerModule.DTO;

import java.util.UUID;

import gpsUtil.location.Attraction;
import lombok.Data;

@Data
public class AttractionDTO {
    private String attractionName;
    private String city;
    private String state;
    private UUID attractionId;
    private double latitude;
    private double longitude;

    public AttractionDTO() {}

    public AttractionDTO(Attraction attraction) {
        this.attractionName = attraction.attractionName;
        this.city = attraction.city;
        this.state = attraction.state;
        this.attractionId = attraction.attractionId;
        this.latitude = attraction.latitude;
        this.longitude = attraction.longitude;
    }
}
