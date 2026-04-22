package com.suman.project.hotelBooking.Strategy.PriceingStrategyImplementation;

import com.suman.project.hotelBooking.Strategy.PricingStrategy;
import com.suman.project.hotelBooking.entity.Inventory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PricingService
{
    /**
     * Calculates a dynamic hotel price for the given inventory by composing base pricing with
     * surge, occupancy, urgency, and holiday pricing strategies.
     *
     * @param inventory the inventory item (room/date/context) for which to compute the price
     * @return the computed dynamic price as a BigDecimal
     */
    public BigDecimal calculateDynamicPrice(Inventory inventory)
    {
        PricingStrategy pricingStrategy = new BasePricingStrategy();

        // Apply the additional strategy
          pricingStrategy = new SurgePricingStrategy(pricingStrategy);
          pricingStrategy = new OccupancyPricingStrategy(pricingStrategy);
          pricingStrategy = new UrgencyPricingStrategy(pricingStrategy);
          pricingStrategy = new HolidayPricingStrategy(pricingStrategy);

          return pricingStrategy.calculatePrice(inventory);

    }
}
