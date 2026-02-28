package com.suman.project.hotelBooking.repository;

import com.suman.project.hotelBooking.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface HotelRepository extends JpaRepository<Hotel, Long>
{

}
