package com.suman.project.hotelBooking.service;

import com.suman.project.hotelBooking.entity.Booking;

public interface CheckoutService
{
    String getCheckoutSession(Booking booking, String successUrl , String failUrl);
}
