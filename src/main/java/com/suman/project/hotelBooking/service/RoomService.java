package com.suman.project.hotelBooking.service;


import com.suman.project.hotelBooking.dto.RoomDto;

import java.util.List;

public interface RoomService
{
    RoomDto createNewRoom(RoomDto roomDto , Long hotelId);
    List<RoomDto> getAllRoomsInTheHotel(Long hotelId);
    RoomDto getRoomById(Long id);
    void deleteRoomById(Long id);

    RoomDto updateRoomById(Long hotelId, Long roomId, RoomDto roomDto);
}
