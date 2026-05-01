package com.suman.project.hotelBooking.Strategy.PriceingStrategyImplementation;

import com.suman.project.hotelBooking.Strategy.PricingStrategy;
import com.suman.project.hotelBooking.entity.Inventory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PricingService
{
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


    // Return the sum of price of this inventory list
    public BigDecimal calculateTotalPrice(List<Inventory>inventoryList )
    {
        return inventoryList.stream()
                .map(this::calculateDynamicPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);


    }
}
