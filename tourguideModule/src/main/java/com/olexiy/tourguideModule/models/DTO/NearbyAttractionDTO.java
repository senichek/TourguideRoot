package com.olexiy.tourguideModule.models.DTO;

import gpsUtil.location.Location;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NearbyAttractionDTO {
    private String attractionName;
    private Location attractionLocation;
    private Location userLocation;
    private Double distanceInMiles;
    private int rewardsPoints;
}
