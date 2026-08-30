package com.example.airBnbApp.Strategy;

import com.example.airBnbApp.entity.Inventory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PricingService {

    public BigDecimal calculateDynamicPricing(Inventory inventory){
        PricingStrategy pricingStrategy=new BasePricingStrategy();

        //apply additional strategies
        pricingStrategy=new SurgePricingStrategy(pricingStrategy);
        pricingStrategy=new OccupancyPricingStrategy(pricingStrategy);
        pricingStrategy=new UrgencyPriceStrategy(pricingStrategy);
        pricingStrategy=new HolidayPricing(pricingStrategy);

        return pricingStrategy.calculatePrice(inventory);
    }
}
