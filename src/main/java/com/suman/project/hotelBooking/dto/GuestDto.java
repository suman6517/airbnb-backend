package com.suman.project.hotelBooking.dto;

import com.suman.project.hotelBooking.entity.Booking;
import com.suman.project.hotelBooking.entity.User;
import com.suman.project.hotelBooking.entity.enums.Gender;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;
@Data
public class GuestDto
{
    private Long id;
    private User user;
    private String name;
    private Gender gender;
    private Integer age;
    private Set<Booking> bookings;

}
