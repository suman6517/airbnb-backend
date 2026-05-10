package com.suman.project.hotelBooking.controller;

import com.suman.project.hotelBooking.dto.BookingDto;
import com.suman.project.hotelBooking.dto.HotelDto;
import com.suman.project.hotelBooking.dto.HotelReportDto;
import com.suman.project.hotelBooking.service.BookingService;
import com.suman.project.hotelBooking.service.HotelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/admin/hotels")
@RequiredArgsConstructor
@Slf4j
public class HotelController
{
    private final HotelService hotelService;
    private final BookingService bookingService;

    @PostMapping()
    public ResponseEntity<HotelDto> createNewHotel(@RequestBody HotelDto hotelDto)
    {
        log.info("Attempting to create hotel with name : {}", hotelDto.getName());

        HotelDto hotel = hotelService.createHotel(hotelDto);

        return new ResponseEntity<>(hotel, HttpStatus.CREATED);

    }

    @GetMapping("/{hotelId}")
    public ResponseEntity<HotelDto> getHotelById(@PathVariable Long hotelId)
    {
        HotelDto hotel = hotelService.getHotelById(hotelId);

        return new ResponseEntity<>(hotel, HttpStatus.OK);
    }

    @PutMapping("/{hotelId}")
    public ResponseEntity<HotelDto> updateHotelById(@PathVariable Long hotelId, @RequestBody HotelDto hotelDto)
    {
        HotelDto updatedHotel = hotelService.updateHotel(hotelDto , hotelId );
        return ResponseEntity.ok(updatedHotel);
    }

    @DeleteMapping("/{hotelId}")
    public ResponseEntity<Void> deleteHotelById(@PathVariable Long hotelId)
    {
        hotelService.deleteHotelById(hotelId );
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{hotelId}")
    public ResponseEntity<Void> activateHotel(@PathVariable Long hotelId)
    {
        hotelService.activateHotel(hotelId);
        return ResponseEntity.noContent().build();
    }
    @GetMapping
    public ResponseEntity<List<HotelDto>> getAllHotels()
    {
        return ResponseEntity.ok(hotelService.getAllHotels());
    }

    @GetMapping("/{hotelId}/bookings")
    public ResponseEntity<List<BookingDto>> getAllBookingsByHotelId(@PathVariable Long hotelId)
    {
        return ResponseEntity.ok(bookingService.getAllBookingsByHotelId(hotelId));
    }

    @GetMapping("/{hotelId}/reports")
    public ResponseEntity<HotelReportDto> getReportOfHotelByHotelId(@PathVariable Long hotelId ,
                                                                    @RequestParam(required = false)LocalDate startDate,
                                                                    @RequestParam(required = false)LocalDate endDate)
    {
        if(startDate == null )
        {
            startDate = LocalDate.now().minusMonths(1);
        }
        if(endDate == null )
        {
            endDate = LocalDate.now();
        }
        return ResponseEntity.ok(bookingService.getHotelReport(hotelId ,startDate , endDate));
    }

}
