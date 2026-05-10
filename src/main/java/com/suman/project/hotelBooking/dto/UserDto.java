package com.suman.project.hotelBooking.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserDto
{
    private Long id;
    private String email;
    private String name;
    private String gender;
    private LocalDate birthday;
}
