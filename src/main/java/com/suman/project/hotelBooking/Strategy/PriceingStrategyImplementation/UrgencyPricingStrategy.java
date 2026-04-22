package com.suman.project.hotelBooking.Strategy.PriceingStrategyImplementation;

import com.suman.project.hotelBooking.Strategy.PricingStrategy;
import com.suman.project.hotelBooking.entity.Inventory;
import lombok.RequiredArgsConstructor;


import java.math.BigDecimal;
import java.time.LocalDate;

@RequiredArgsConstructor
public class UrgencyPricingStrategy implements PricingStrategy
{
    private final PricingStrategy wrapped;

    /**
     * Applies an urgency adjustment to an inventory's price when the inventory date falls within the next seven days.
     *
     * If the inventory's date is on or after today and before seven days from today, the returned price is increased by 15%.
     *
     * @param inventory the inventory item whose date and base price are used to compute the final price
     * @return the final price, increased by 15% when the inventory date is today (inclusive) through six days from today (inclusive), otherwise the base price
     */
    @Override
    public BigDecimal calculatePrice(Inventory inventory)
    {
        BigDecimal price = wrapped.calculatePrice(inventory);
        LocalDate today = LocalDate.now();

        if(!inventory.getDate().isBefore(today) && inventory.getDate().isBefore(today.plusDays(7)))
        {
            price = price.multiply(BigDecimal.valueOf(1.15));
        }
        return price;
    }
}
