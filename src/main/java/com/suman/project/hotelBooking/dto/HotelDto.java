package com.suman.project.hotelBooking.dto;

import com.suman.project.hotelBooking.entity.HotelContactInfo;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
public class HotelDto
{

    private Long id;

    private String name;

    private String city;

    private String[] photo;

    private String[] amenities;

    private HotelContactInfo contactInfo;

    private Boolean active;
}
