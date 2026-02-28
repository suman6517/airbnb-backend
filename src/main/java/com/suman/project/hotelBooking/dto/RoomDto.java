package com.suman.project.hotelBooking.dto;

import lombok.Data;

import java.math.BigDecimal;


@Data
public class RoomDto
{
    private Long id;
    private String type;
    private BigDecimal price;
    private String[] photo;
    private String[] amenities;
    private Integer totalCount;
    private Integer capacity;
}
