package com.suman.project.hotelBooking.dto;

import com.suman.project.hotelBooking.entity.enums.Gender;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserProfileUpdateRequestDto
{
    private String name;
    private LocalDate dateOfBirth;
    private Gender gender;
}
