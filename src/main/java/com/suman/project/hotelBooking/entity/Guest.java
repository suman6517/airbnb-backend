package com.suman.project.hotelBooking.entity;


import com.suman.project.hotelBooking.entity.enums.Gender;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Entity
public class Guest
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private Integer age;

//    @CreationTimestamp
//    private LocalDateTime createTime;
//
//    @UpdateTimestamp
//    private LocalDateTime updateTime;

    @ManyToMany(mappedBy = "guests")
    private Set<Booking> bookings;



}
