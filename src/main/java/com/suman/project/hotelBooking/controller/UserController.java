package com.suman.project.hotelBooking.controller;

import com.suman.project.hotelBooking.dto.BookingDto;
import com.suman.project.hotelBooking.dto.UserDto;
import com.suman.project.hotelBooking.dto.UserProfileUpdateRequestDto;
import com.suman.project.hotelBooking.service.BookingService;
import com.suman.project.hotelBooking.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController
{
    private final UserService userService;
    private final BookingService bookingService;

    @PatchMapping("/profile")
    public ResponseEntity<Void> updateProfile(@RequestBody UserProfileUpdateRequestDto userProfileUpdateRequestDto)
    {
        userService.updateProfile(userProfileUpdateRequestDto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/myBookings")
    public ResponseEntity<List<BookingDto>> getMyBookings()
    {
        return ResponseEntity.ok(bookingService.getMyBookings());
    }

    @GetMapping
    public ResponseEntity<UserDto> getMyProfile()
    {
        return ResponseEntity.ok(userService.getMyProfile());
    }
}
