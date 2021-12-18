package com.olexiy.tourguideModule.models.DTO;

import com.olexiy.tourguideModule.models.AttractionDTO;
import com.olexiy.tourguideModule.models.UserReward;

import lombok.Data;

@Data
public class UserRewardDTO {
    private VisitedLocationDTO visitedLocationDTO;
	private AttractionDTO attractionDTO;
	private int rewardPoints;

    public UserRewardDTO() {}

    public UserRewardDTO(UserReward userReward) {
        this.visitedLocationDTO = new VisitedLocationDTO(userReward.visitedLocation);
        this.attractionDTO = new AttractionDTO(userReward.attraction);
        this.rewardPoints = userReward.getRewardPoints();
    }
}
