package com.suman.project.hotelBooking.Strategy;

import com.suman.project.hotelBooking.entity.Inventory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


public interface PricingStrategy
{
    BigDecimal calculatePrice(Inventory inventory);
}
