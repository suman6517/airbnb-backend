package com.suman.project.hotelBooking.controller;

import com.suman.project.hotelBooking.dto.HotelDto;
import com.suman.project.hotelBooking.dto.HotelInfoDto;
import com.suman.project.hotelBooking.dto.HotelSearchRequestDto;
import com.suman.project.hotelBooking.service.HotelService;
import com.suman.project.hotelBooking.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/hotels")
@RequiredArgsConstructor
public class HotelBrowseController
{
    private final InventoryService inventoryService;
    private final HotelService hotelService;

    @GetMapping("/search")
    public ResponseEntity<Page<HotelDto>> searchHotels(@RequestBody HotelSearchRequestDto hotelSearchRequestDto)
    {
        Page<HotelDto> page = inventoryService.searchHotels(hotelSearchRequestDto);
        return ResponseEntity.ok(page);

    }
    @GetMapping("/{hotelId}/info")
    public ResponseEntity<HotelInfoDto> getHotelInfo(@PathVariable Long hotelId)
    {
        return ResponseEntity.ok(hotelService.getHotelInfoById(hotelId));
    }

}
