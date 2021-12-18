package com.olexiy.tourguideModule.config;

import com.olexiy.tourguideModule.services.RewardsService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.zalando.jackson.datatype.money.MoneyModule;

import gpsUtil.GpsUtil;
import rewardCentral.RewardCentral;


@Configuration
public class TourguideModuleConfig {
	
	@Bean
	public GpsUtil getGpsUtil() {
		return new GpsUtil();
	}
	
	@Bean
	public RewardsService getRewardsService() {
		return new RewardsService(getGpsUtil(), getRewardCentral());
	}
	
	@Bean
	public RewardCentral getRewardCentral() {
		return new RewardCentral();
	}

	@Bean
	public WebClient getWebClient() {
		return WebClient.builder()
        .baseUrl("http://localhost:8081")
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .build();
	}

	@Bean
    public MoneyModule moneyModule() {
        return new MoneyModule();
    }
}