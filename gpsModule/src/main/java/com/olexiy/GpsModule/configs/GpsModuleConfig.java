package com.olexiy.GpsModule.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.zalando.jackson.datatype.money.MoneyModule;

import gpsUtil.GpsUtil;
import rewardCentral.RewardCentral;

@Configuration
public class GpsModuleConfig {
    @Bean
	public GpsUtil getGpsUtil() {
		return new GpsUtil();
	}
	
	@Bean
	public RewardCentral getRewardCentral() {
		return new RewardCentral();
	}

	/* In this project we have 5 different modules. Each module is hosted in a seperate Docker 
	container. In order to access the different containers we use the same URL which 
	is <http://host.docker.internal> but with different ports. Each module listens on a 
	specific port (see EXPOSE in Dockerfile). If you need this app to run this project 
	locally (outside of Docker) you can use the URL <http://localhost:Port_Number> */
	
	@Bean
	public WebClient getWebClient() {
		return WebClient.builder()
        .baseUrl("http://host.docker.internal:8082")
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .build();
	}

	@Bean
    public MoneyModule moneyModule() {
        return new MoneyModule();
    }
}
