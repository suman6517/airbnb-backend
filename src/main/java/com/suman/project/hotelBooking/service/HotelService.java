package com.suman.project.hotelBooking.service;

import com.suman.project.hotelBooking.dto.HotelDto;
import com.suman.project.hotelBooking.dto.HotelInfoDto;
import com.suman.project.hotelBooking.entity.Hotel;

import java.util.List;

public interface HotelService
{
    HotelDto createHotel (HotelDto hotelDto);

    HotelDto getHotelById (Long id);

    HotelDto updateHotel (HotelDto hotelDto , Long id);

    Boolean deleteHotelById (Long id);

    void activateHotel (Long id);


    HotelInfoDto getHotelInfoById(Long hotelId);

    List<HotelDto> getAllHotels();
}
