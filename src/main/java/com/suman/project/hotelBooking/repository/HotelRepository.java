package com.suman.project.hotelBooking.repository;

import com.suman.project.hotelBooking.entity.Hotel;
import com.suman.project.hotelBooking.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface HotelRepository extends JpaRepository<Hotel, Long>
{

    List<Hotel> findByOwner(User user);
}
