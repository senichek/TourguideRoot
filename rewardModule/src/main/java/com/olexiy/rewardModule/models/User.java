package com.olexiy.rewardModule.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

import com.olexiy.rewardModule.models.DTO.UserDTO;

import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import tripPricer.Provider;

public class User {
	private final UUID userId;
	private final String userName;
	private String phoneNumber;
	private String emailAddress;
	private Date latestLocationTimestamp;
	private List<VisitedLocation> visitedLocations = new CopyOnWriteArrayList<>();
	private List<UserReward> userRewards = new CopyOnWriteArrayList<>();
	private UserPreferences userPreferences = new UserPreferences();
	private List<Provider> tripDeals = new ArrayList<>();
	public User(UUID userId, String userName, String phoneNumber, String emailAddress) {
		this.userId = userId;
		this.userName = userName;
		this.phoneNumber = phoneNumber;
		this.emailAddress = emailAddress;
	}

	public User(UserDTO userDTO) {
        this.userId = userDTO.getUserId();
        this.userName = userDTO.getUserName();
        this.phoneNumber = userDTO.getPhoneNumber();
        this.emailAddress = userDTO.getEmailAddress();
        this.latestLocationTimestamp = userDTO.getLatestLocationTimestamp();
    
	  userDTO.getVisitedLocations().forEach(loc -> {
		  Location location = new Location(loc.getLocationDTO().getLatitude(), loc.getLocationDTO().getLongitude());
		  VisitedLocation visitedLocation = new VisitedLocation(userId, location, loc.getTimeVisited());
		  this.visitedLocations.add(visitedLocation);
	  });

	  userDTO.getUserRewards().forEach(rew -> {
		Location location = new Location(rew.getVisitedLocationDTO().getLocationDTO().getLatitude(), rew.getVisitedLocationDTO().getLocationDTO().getLongitude());
		VisitedLocation visitedLocation = new VisitedLocation(userId, location, rew.getVisitedLocationDTO().getTimeVisited());
		Attraction attraction = new Attraction(rew.getAttractionDTO().getAttractionName(), rew.getAttractionDTO().getCity(), rew.getAttractionDTO().getState(), rew.getAttractionDTO().getLatitude(), rew.getAttractionDTO().getLongitude());
		UserReward userReward = new UserReward(visitedLocation, attraction);
		this.userRewards.add(userReward);
	  });
    }
	
	public UUID getUserId() {
		return userId;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	
	public String getEmailAddress() {
		return emailAddress;
	}
	
	public void setLatestLocationTimestamp(Date latestLocationTimestamp) {
		this.latestLocationTimestamp = latestLocationTimestamp;
	}
	
	public Date getLatestLocationTimestamp() {
		return latestLocationTimestamp;
	}
	
	public void addToVisitedLocations(VisitedLocation visitedLocation) {
		visitedLocations.add(visitedLocation);
	}
	
	public List<VisitedLocation> getVisitedLocations() {
		return visitedLocations;
	}
	
	public void clearVisitedLocations() {
		visitedLocations.clear();
	}

	public void addUserReward(UserReward userReward) {
			userRewards.add(userReward);

	}
	
	public List<UserReward> getUserRewards() {
		return userRewards;
	}
	
	public UserPreferences getUserPreferences() {
		return userPreferences;
	}
	
	public void setUserPreferences(UserPreferences userPreferences) {
		this.userPreferences = userPreferences;
	}

	public VisitedLocation getLastVisitedLocation() {
		return visitedLocations.get(visitedLocations.size() - 1);
	}
	
	public void setTripDeals(List<Provider> tripDeals) {
		this.tripDeals = tripDeals;
	}
	
	public List<Provider> getTripDeals() {
		return tripDeals;
	}
}
