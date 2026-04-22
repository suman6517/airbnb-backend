package com.suman.project.hotelBooking.service;

import com.suman.project.hotelBooking.entity.Hotel;
import org.springframework.stereotype.Service;

public interface PricingUpdateService
{
    /**
 * Triggers an hourly update of inventory and HotelMinPrice table entries.
 *
 * Implementations perform the necessary updates to inventory and minimum-price records for hotels.
 */

    public void UpdatePrices();
}
