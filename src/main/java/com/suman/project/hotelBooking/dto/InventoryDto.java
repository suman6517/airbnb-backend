package com.suman.project.hotelBooking.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data

public class InventoryDto
{
    private Long id;
    private LocalDate date;
    private Integer bookCount;
    private Integer reservedCount;
    private Integer totalCount;
    private BigDecimal surgeFactor;
    private BigDecimal price;
    private Boolean closed;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
