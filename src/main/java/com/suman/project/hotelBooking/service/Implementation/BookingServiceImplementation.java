package com.suman.project.hotelBooking.service.Implementation;

import com.suman.project.hotelBooking.dto.BookingDto;
import com.suman.project.hotelBooking.dto.BookingRequestDto;
import com.suman.project.hotelBooking.dto.GuestDto;
import com.suman.project.hotelBooking.entity.*;
import com.suman.project.hotelBooking.entity.enums.BookingStatus;
import com.suman.project.hotelBooking.exception.ResourceNotFoundException;
import com.suman.project.hotelBooking.repository.*;
import com.suman.project.hotelBooking.service.BookingService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.awt.print.Book;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingServiceImplementation implements BookingService
{
    private final GuestRepository guestRepository;
    private final HotelBookingRepository hotelBookingRepository;
    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;
    private final InventoryRepository inventoryRepository;
    private final ModelMapper modelMapper;
    @Override
    @Transactional
    public BookingDto initializeBooking(BookingRequestDto bookingRequestDto)
    {

        log.info("Initializing Booking for hotel : {} , room : {} , date : {}-{}" , bookingRequestDto.getHotelId() , bookingRequestDto.getRoomId()
                , bookingRequestDto.getCheckInDate() ,  bookingRequestDto.getCheckOutDate());


        Hotel hotel = hotelRepository.findById(bookingRequestDto.getHotelId())
                .orElseThrow(() -> new ResourceNotFoundException("Hotel Not Found with id: " + bookingRequestDto.getHotelId()));


        Room room = roomRepository.findById(bookingRequestDto.getRoomId())
                .orElseThrow(() -> new ResourceNotFoundException("Room Not Found with id: " + bookingRequestDto.getRoomId()));

        List<Inventory> inventoryList = inventoryRepository.findAndLockAvailableInventory(room.getId() ,
                bookingRequestDto.getCheckInDate() , bookingRequestDto.getCheckOutDate() , bookingRequestDto.getRoomsCount());

        long daysCount = ChronoUnit.DAYS.between(bookingRequestDto.getCheckInDate(), bookingRequestDto.getCheckOutDate())+1;

        if(inventoryList.size() != daysCount)
        {
            throw  new IllegalStateException("Room Is Not available anymore");
        }


        // Reserve the room / update the bookCount of Inventory's

        for(Inventory inventory : inventoryList)
        {
            inventory.setReservedCount(inventory.getReservedCount() + bookingRequestDto.getRoomsCount());
        }

        inventoryRepository.saveAll(inventoryList);

        // Create the booking

        // TODO: Calculate Dynamic amount

        Booking booking = Booking.builder()
                .bookingStatus(BookingStatus.RESERVED)
                .hotel(hotel)
                .room(room)
                .checkInDate(bookingRequestDto.getCheckInDate())
                .checkOutDate(bookingRequestDto.getCheckOutDate())
                .user(getCurrentUser())
                .romsCount(bookingRequestDto.getRoomsCount())
                .amount(BigDecimal.TEN)
                .build();

        booking = hotelBookingRepository.save(booking);

        return modelMapper.map(booking, BookingDto.class);
    }

    @Override
    @Transactional
    public BookingDto addGuest(Long bookingId, List<GuestDto> guestDtoList)
    {
        log.info("Adding guest for booking with id : {} ", bookingId);

        Booking booking = hotelBookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel Not Found with id: " + bookingId));

        if(hasBookingExpired(booking))
        {
            throw new IllegalStateException("Booking has already expired");
        }

        if(booking.getBookingStatus() != BookingStatus.RESERVED)
        {
            throw new IllegalStateException("Booking is not under RESERVED state , cannot add guest");
        }

        for(GuestDto guestDto : guestDtoList)
        {
            Guest guest = modelMapper.map(guestDto, Guest.class);
            guest.setUser(getCurrentUser());
            guest = guestRepository.save(guest);
            booking.getGuests().add(guest);
        }

        booking.setBookingStatus(BookingStatus.GUEST_ADDED);
        booking = hotelBookingRepository.save(booking);

        return modelMapper.map(booking, BookingDto.class);

    }

    public boolean hasBookingExpired(Booking booking)
    {
        return booking.getCreateTime().plusMinutes(10).isBefore(LocalDateTime.now());
    }

    public User getCurrentUser()
    {
        User user = new User();
        user.setId(1L);  // TODO: Remove the dummy user
        return user;
    }
}
