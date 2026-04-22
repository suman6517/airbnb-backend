package com.suman.project.hotelBooking.Strategy.PriceingStrategyImplementation;

import com.suman.project.hotelBooking.Strategy.PricingStrategy;
import com.suman.project.hotelBooking.entity.Inventory;

import java.math.BigDecimal;

public class BasePricingStrategy implements PricingStrategy
{

    @Override
    public BigDecimal calculatePrice(Inventory inventory)
    {
        return inventory.getRoom().getPrice();
    }
}
