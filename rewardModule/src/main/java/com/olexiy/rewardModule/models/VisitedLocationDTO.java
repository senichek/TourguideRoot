package com.olexiy.rewardModule.models;

import java.util.Date;
import java.util.UUID;

import gpsUtil.location.VisitedLocation;
import lombok.Data;

@Data
public class VisitedLocationDTO {
    private UUID userId;
    private LocationDTO locationDTO;
    private Date timeVisited;

    public VisitedLocationDTO() {
    }

    public VisitedLocationDTO(VisitedLocation visitedLocation) {
        this.userId = visitedLocation.userId;
        this.locationDTO = new LocationDTO(visitedLocation.location);
        this.timeVisited = visitedLocation.timeVisited;
    }
}