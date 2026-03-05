package com.suman.project.hotelBooking.controller;


import com.suman.project.hotelBooking.dto.BookingDto;
import com.suman.project.hotelBooking.dto.BookingRequestDto;
import com.suman.project.hotelBooking.dto.GuestDto;
import com.suman.project.hotelBooking.service.BookingService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bookings")
public class HotelBookingController
{
    private final BookingService bookingService;

    @PostMapping("/init")
    public ResponseEntity<BookingDto> initializeBooking(@RequestBody BookingRequestDto bookingRequestDto)
    {
        return ResponseEntity.ok(bookingService.initializeBooking(bookingRequestDto));
    }

    @PostMapping("/{bookingId}/addGuests")
    public ResponseEntity<BookingDto> addGuest(@RequestBody List<GuestDto> guestDtoList ,@PathVariable Long bookingId)
    {
        return ResponseEntity.ok(bookingService.addGuest(bookingId , guestDtoList));
    }

}
