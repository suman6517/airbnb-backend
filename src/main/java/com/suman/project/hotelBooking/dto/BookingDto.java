package com.suman.project.hotelBooking.dto;

import com.suman.project.hotelBooking.entity.Guest;
import com.suman.project.hotelBooking.entity.Hotel;
import com.suman.project.hotelBooking.entity.Room;
import com.suman.project.hotelBooking.entity.User;
import com.suman.project.hotelBooking.entity.enums.BookingStatus;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Data
public class BookingDto
{
    private Long id;
    private Integer romsCount;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private BookingStatus bookingStatus;
    private Set<Guest> guests;
}
