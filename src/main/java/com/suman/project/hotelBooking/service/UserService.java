package com.suman.project.hotelBooking.service;

import com.suman.project.hotelBooking.dto.UserDto;
import com.suman.project.hotelBooking.dto.UserProfileUpdateRequestDto;
import com.suman.project.hotelBooking.entity.User;

public interface UserService
{
    User getUserById(Long id);

    void updateProfile(UserProfileUpdateRequestDto userProfileUpdateRequestDto);

    UserDto getMyProfile();
}
