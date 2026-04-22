package com.suman.project.hotelBooking.service;

import com.suman.project.hotelBooking.entity.Hotel;
import org.springframework.stereotype.Service;

public interface PricingUpdateService
{
    // Schedular to update the inventory and HotelMinPrice tables every hour

    public void UpdatePrices();
}
