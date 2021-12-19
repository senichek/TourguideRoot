package com.olexiy.GpsModule.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.olexiy.GpsModule.models.DTO.AttractionDTO;
import com.olexiy.GpsModule.models.DTO.VisitedLocationDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;

@RestController
public class GpsController {

    @Autowired
    private GpsUtil gpsUtil;

    @GetMapping(value = "/getUserLocation")
    public VisitedLocationDTO getUserLocation(@RequestParam(name = "userId") UUID userId) {
        VisitedLocation userLocation = gpsUtil.getUserLocation(userId);
        return new VisitedLocationDTO(userLocation);
    }

    @GetMapping(value = "/getAttractions")
    public List<AttractionDTO> getAttractions() {
        List<Attraction> attractions = gpsUtil.getAttractions();
        List<AttractionDTO> attractionDTOs = new ArrayList<>();

        attractions.forEach(attr -> {
            attractionDTOs.add(new AttractionDTO(attr));
        });

        return attractionDTOs;
    }
}
