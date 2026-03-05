package com.suman.project.hotelBooking.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class HotelSearchRequestDto
{
    private String city;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer roomCount;

    private Integer page=0;
    private Integer pageSize=10;
}
