package com.suman.project.hotelBooking.Strategy.PriceingStrategyImplementation;

import com.suman.project.hotelBooking.Strategy.PricingStrategy;
import com.suman.project.hotelBooking.entity.Inventory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


@RequiredArgsConstructor
public class OccupancyPricingStrategy implements PricingStrategy
{
    private final PricingStrategy wrapped;

    /**
     * Adjusts the delegated base price according to the inventory's occupancy.
     *
     * @param inventory the inventory whose booked and total counts are used to compute occupancy and the base price
     * @return the price after applying a 20% increase when occupancy is greater than 0.8, otherwise the base price
     */
    @Override
    public BigDecimal calculatePrice(Inventory inventory)
    {
        BigDecimal price = wrapped.calculatePrice(inventory);
        double occupancyRate = (double)inventory.getBookCount() / inventory.getTotalCount();
        if(occupancyRate > 0.8)
        {
            price = price.multiply(BigDecimal.valueOf(1.2));
        }
        return price;
    }
}
