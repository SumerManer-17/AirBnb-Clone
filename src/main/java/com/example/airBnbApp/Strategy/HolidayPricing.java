package com.example.airBnbApp.Strategy;

import com.example.airBnbApp.entity.Inventory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
@Service
@RequiredArgsConstructor
public class HolidayPricing implements PricingStrategy{
    private final PricingStrategy wrapped;

    @Override
    public BigDecimal calculatePrice(Inventory inventory) {
        BigDecimal price=wrapped.calculatePrice(inventory);

        boolean isTodayHoliday=true; //call API and check with local data

        if (isTodayHoliday){
            price=price.multiply(BigDecimal.valueOf(1.25));
        }

        return price;
    }
}
