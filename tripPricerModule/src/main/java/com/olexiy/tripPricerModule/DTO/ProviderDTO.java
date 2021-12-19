package com.olexiy.tripPricerModule.DTO;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tripPricer.Provider;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProviderDTO {
    private String name;
    private Double price;
    private UUID tripId;

    public ProviderDTO(Provider provider) {
        this.name = provider.name;
        this.price = provider.price;
        this.tripId = provider.tripId;
    }
}
