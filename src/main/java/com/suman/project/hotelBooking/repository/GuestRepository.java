package com.suman.project.hotelBooking.repository;

import com.suman.project.hotelBooking.entity.Guest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuestRepository extends JpaRepository<Guest, Long>
{
}