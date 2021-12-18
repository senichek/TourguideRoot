package com.olexiy.tourguideModule.services;

import java.util.List;

import com.olexiy.tourguideModule.models.DTO.UserDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class RewardsServiceWEB {

    @Autowired
	private WebClient webClient;

    private Logger logger = LoggerFactory.getLogger(RewardsService.class);
    
    public List<UserDTO> calculateRewards(List<UserDTO> users) {
        List<UserDTO> bodyToMono = webClient.post()
        .uri("/calculateRewards")
		.body(BodyInserters.fromObject(users))
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<List<UserDTO>>() {})
		.block();
		logger.debug("<<calculateRewards via WEB>> was called");
        return bodyToMono;
	}
}
