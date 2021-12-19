package com.olexiy.tripPricerModule.DTO;

import gpsUtil.location.Location;
import lombok.Data;

@Data
public class LocationDTO {
    private double longitude;
    private double latitude;

    public LocationDTO() {}

    public LocationDTO(Location location) {
        this.longitude = location.longitude;
        this.latitude = location.latitude;
    } 
}
