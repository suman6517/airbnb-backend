package com.suman.project.hotelBooking.dto;

import com.suman.project.hotelBooking.entity.Hotel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotelPriceDto
{
    private Hotel hotel;
    private  Double price;
}
