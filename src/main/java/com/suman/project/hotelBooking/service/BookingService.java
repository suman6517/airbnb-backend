package com.suman.project.hotelBooking.service;

import com.stripe.model.Event;
import com.suman.project.hotelBooking.dto.BookingDto;
import com.suman.project.hotelBooking.dto.BookingRequestDto;
import com.suman.project.hotelBooking.dto.GuestDto;

import java.util.List;
import java.util.Map;

public interface BookingService
{

    BookingDto initializeBooking(BookingRequestDto bookingRequestDto);

    BookingDto addGuest(Long bookingId, List<GuestDto> guestDtoList);

    String initiatePayment(Long bookingId);

    void capturePayment(Event event ,String payload);

    void cancelBooking(Long bookingId);

    String getBookingStatus(Long bookingId);
}
