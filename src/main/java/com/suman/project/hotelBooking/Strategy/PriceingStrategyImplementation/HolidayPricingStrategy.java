package com.suman.project.hotelBooking.Strategy.PriceingStrategyImplementation;

import com.suman.project.hotelBooking.Strategy.PricingStrategy;
import com.suman.project.hotelBooking.entity.Inventory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


@RequiredArgsConstructor
public class HolidayPricingStrategy  implements PricingStrategy
{

    private final PricingStrategy pricingStrategy;

    @Override
    public BigDecimal calculatePrice(Inventory inventory)
    {
        BigDecimal price = pricingStrategy.calculatePrice(inventory);
        boolean isTodayHoliday = true; // TODO: Call an api handle it and check if it is a Holiday or Not
        if(isTodayHoliday)
        {
            price = price.multiply(BigDecimal.valueOf(1.25));
        }
        return price;
    }
}
