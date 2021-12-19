package com.olexiy.rewardCentralModule.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import rewardCentral.RewardCentral;

@Configuration
public class RewardCentralModuleConfig {

    @Bean
	public RewardCentral getRewardCentral() {
		return new RewardCentral();
	}
}
