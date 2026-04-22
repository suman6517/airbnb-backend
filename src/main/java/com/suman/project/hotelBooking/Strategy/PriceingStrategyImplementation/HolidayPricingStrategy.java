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

    /**
     * Calculate the booking price for the given inventory and apply a holiday markup when applicable.
     *
     * @param inventory the inventory item (room/booking context) to price
     * @return the final price for the provided inventory after applying a 25% holiday markup when the date is a holiday
     */
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
