package com.suman.project.hotelBooking.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class HotelMinPrice
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id" , nullable = false)
    private Hotel hotel;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false , precision = 10 , scale = 2)
    private BigDecimal price; // Cheapest Room Price On a particular day

    @CreationTimestamp
    private LocalDateTime createTime;

    @UpdateTimestamp
    private LocalDateTime updateTime;


    public HotelMinPrice(Hotel hotel, LocalDate date)
    {
        this.hotel = hotel;
        this.date = date;
    }
}
