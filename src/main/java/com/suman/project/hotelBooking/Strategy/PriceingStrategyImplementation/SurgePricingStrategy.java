package com.suman.project.hotelBooking.Strategy.PriceingStrategyImplementation;

import com.suman.project.hotelBooking.Strategy.PricingStrategy;
import com.suman.project.hotelBooking.entity.Inventory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@RequiredArgsConstructor
public class SurgePricingStrategy implements PricingStrategy
{
    private final PricingStrategy wrapped;

    /**
     * Applies the inventory's surge factor to the calculated price and returns the adjusted amount.
     *
     * @param inventory the inventory whose surge factor will be applied to adjust the price
     * @return the price multiplied by the inventory's surge factor
     */
    @Override
    public BigDecimal calculatePrice(Inventory inventory)
    {
        return wrapped.calculatePrice(inventory).multiply(inventory.getSurgeFactor());
    }
}
