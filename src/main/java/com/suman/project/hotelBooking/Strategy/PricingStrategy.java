package com.suman.project.hotelBooking.Strategy;

import com.suman.project.hotelBooking.entity.Inventory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


public interface PricingStrategy
{
    /**
 * Calculate the price for the given inventory.
 *
 * @param inventory the inventory data used to determine pricing
 * @return the calculated price as a BigDecimal
 */
BigDecimal calculatePrice(Inventory inventory);
}
