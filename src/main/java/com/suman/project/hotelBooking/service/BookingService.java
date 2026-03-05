package com.suman.project.hotelBooking.service;

import com.suman.project.hotelBooking.dto.BookingDto;
import com.suman.project.hotelBooking.dto.BookingRequestDto;
import com.suman.project.hotelBooking.dto.GuestDto;

import java.util.List;

public interface BookingService
{

    BookingDto initializeBooking(BookingRequestDto bookingRequestDto);

    BookingDto addGuest(Long bookingId, List<GuestDto> guestDtoList);
}
