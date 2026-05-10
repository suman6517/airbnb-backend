package com.suman.project.hotelBooking.controller;

import com.suman.project.hotelBooking.dto.RoomDto;
import com.suman.project.hotelBooking.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/hotels/{hotelId}/rooms")
@RequiredArgsConstructor
public class RoomAdminController
{
    private final RoomService roomService;

    @PostMapping
    public ResponseEntity<RoomDto> createNewRoom(@RequestBody RoomDto roomDto , @PathVariable Long hotelId)
    {
        RoomDto room = roomService.createNewRoom(roomDto, hotelId);
        return new ResponseEntity<>(room ,HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<RoomDto>> getAllRoomsInTheHotel(@PathVariable Long hotelId)
    {
        return  ResponseEntity.ok(roomService.getAllRoomsInTheHotel(hotelId));
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<RoomDto> getRoomById(@PathVariable Long roomId)
    {
        return  ResponseEntity.ok(roomService.getRoomById(roomId));
    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity<Void> deleteRoomById(@PathVariable Long roomId)
    {
            roomService.deleteRoomById(roomId);
            return ResponseEntity.noContent().build();
    }

    @PutMapping("/{roomId}")
    public ResponseEntity<RoomDto> updateRoomById(@PathVariable Long roomId,@RequestBody RoomDto roomDto ,@PathVariable Long hotelId)
    {
        return ResponseEntity.ok(roomService.updateRoomById(hotelId,roomId,roomDto));

    }


}
