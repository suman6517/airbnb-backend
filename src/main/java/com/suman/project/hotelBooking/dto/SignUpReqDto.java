package com.suman.project.hotelBooking.dto;

import lombok.Data;

@Data
public class SignUpReqDto
{
    private String email;
    private String password;
    private String name;
}
