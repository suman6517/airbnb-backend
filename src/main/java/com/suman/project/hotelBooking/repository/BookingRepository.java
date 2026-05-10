package com.suman.project.hotelBooking.repository;

import com.suman.project.hotelBooking.entity.Booking;
import com.suman.project.hotelBooking.entity.Hotel;
import com.suman.project.hotelBooking.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    Optional<Booking>findByPaymentSessionId(String sessionId);

    List<Booking> findByHotel(Hotel hotel);

    List<Booking> findByHotelAndCreateTimeBetween(Hotel hotel, LocalDateTime start, LocalDateTime end);
    List<Booking>findByUser(User user);
}