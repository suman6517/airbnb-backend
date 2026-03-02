package com.suman.project.hotelBooking.repository;

import com.suman.project.hotelBooking.entity.Inventory;
import com.suman.project.hotelBooking.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface InventoryRepository extends JpaRepository<Inventory, Long>
{
    void deleteByAndRoom( Room room);
}
