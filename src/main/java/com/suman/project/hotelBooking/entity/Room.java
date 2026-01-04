package com.suman.project.hotelBooking.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Room
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "hotel_id" , nullable = false)
    private Hotel hotel;


    @Column(nullable = false , precision = 10 , scale = 2)
    private BigDecimal price;

    @Column(columnDefinition = "TEXT[]")
    private String[] photo;


    @Column(columnDefinition = "TEXT[]")
    private String[] amenities;

    @Column(nullable = false)
    private Integer totalCount;

    @Column(updatable = false)
    private Integer capacity;

    @CreationTimestamp
    private LocalDateTime createTime;

    @UpdateTimestamp
    private LocalDateTime updateTime;

    @Embedded
    private HotelContactInfo contactInfo;
}
