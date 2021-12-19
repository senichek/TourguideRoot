package com.olexiy.tripPricerModule.DTO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.olexiy.tripPricerModule.models.User;

public class UserDTO {
    private UUID userId;
    private String userName;
    private String phoneNumber;
    private String emailAddress;
    private Date latestLocationTimestamp;
    private List<VisitedLocationDTO> visitedLocations = new ArrayList<>();
    private List<UserRewardDTO> userRewards = new ArrayList<>();

    public UserDTO() {
    }

    public UserDTO(User user) {
        this.userId = user.getUserId();
        this.userName = user.getUserName();
        this.phoneNumber = user.getPhoneNumber();
        this.emailAddress = user.getEmailAddress();
        this.latestLocationTimestamp = user.getLatestLocationTimestamp();
        user.getVisitedLocations().forEach(loc -> {
            visitedLocations.add(new VisitedLocationDTO(loc));
        });
        user.getUserRewards().forEach(rew -> {
            userRewards.add(new UserRewardDTO(rew));
        });
    }

    public UUID getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public Date getLatestLocationTimestamp() {
        return latestLocationTimestamp;
    }

    public List<VisitedLocationDTO> getVisitedLocations() {
        return visitedLocations;
    }

    public List<UserRewardDTO> getUserRewards() {
        return userRewards;
    }
}
