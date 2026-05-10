package com.suman.project.hotelBooking.service.Implementation;

import com.suman.project.hotelBooking.dto.UserDto;
import com.suman.project.hotelBooking.dto.UserProfileUpdateRequestDto;
import com.suman.project.hotelBooking.entity.User;
import com.suman.project.hotelBooking.exception.ResourceNotFoundException;
import com.suman.project.hotelBooking.repository.UserRepository;
import com.suman.project.hotelBooking.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static com.suman.project.hotelBooking.utility.AppUtils.getCurrentUser;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImplementation implements UserService , UserDetailsService
{

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() ->new ResourceNotFoundException("User not found with id:" + id));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        return userRepository.findByEmail(username).orElse(null);
    }

    @Override
    public void updateProfile(UserProfileUpdateRequestDto userProfileUpdateRequestDto)
    {
        User user = getCurrentUser();
        if(userProfileUpdateRequestDto.getDateOfBirth() != null)
        {
            user.setBirthday(userProfileUpdateRequestDto.getDateOfBirth());
        }
        if(userProfileUpdateRequestDto.getGender() != null)
        {
            user.setGender(userProfileUpdateRequestDto.getGender());
        }
        if(userProfileUpdateRequestDto.getName()  != null)
        {
            user.setName(userProfileUpdateRequestDto.getName());
        }

        userRepository.save(user);
    }

    @Override
    public UserDto getMyProfile()
    {
        User user = getCurrentUser();
        log.info("Getting User Profile for the User Id {}", user.getId());
        return modelMapper.map(user, UserDto.class);
    }
}
