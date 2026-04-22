package com.suman.project.hotelBooking.Strategy.PriceingStrategyImplementation;

import com.suman.project.hotelBooking.Strategy.PricingStrategy;
import com.suman.project.hotelBooking.entity.Inventory;

import java.math.BigDecimal;

public class BasePricingStrategy implements PricingStrategy
{

    /**
     * Retrieve the room's listed price from the provided inventory.
     *
     * @param inventory the inventory containing the room whose price will be returned
     * @return the room's current listed price
     */
    @Override
    public BigDecimal calculatePrice(Inventory inventory)
    {
        return inventory.getRoom().getPrice();
    }
}
