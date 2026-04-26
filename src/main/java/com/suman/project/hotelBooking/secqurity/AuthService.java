package com.suman.project.hotelBooking.secqurity;

import com.suman.project.hotelBooking.dto.LoginDto;
import com.suman.project.hotelBooking.dto.SignUpReqDto;
import com.suman.project.hotelBooking.dto.UserDto;
import com.suman.project.hotelBooking.entity.User;
import com.suman.project.hotelBooking.entity.enums.Role;
import com.suman.project.hotelBooking.exception.ResourceNotFoundException;
import com.suman.project.hotelBooking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService
{
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;


    public UserDto signUp(SignUpReqDto signUpReqDto)
    {
        User user = userRepository.findByEmail(signUpReqDto.getEmail()).orElse(null);

        if(user != null)
        {
            throw new RuntimeException("User Is already Exist with the same email");
        }

        User newUser = modelMapper.map(signUpReqDto, User.class);

        newUser.setRoles(Set.of(Role.GUEST));
        newUser.setPassword(passwordEncoder.encode(signUpReqDto.getPassword()));

        newUser = userRepository.save(newUser);
        return modelMapper.map(newUser, UserDto.class);
    }

    public String[] login(LoginDto loginDto)
    {
       Authentication authentication =  authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getEmail(), loginDto.getPassword()
        ));

       User user = (User) authentication.getPrincipal();

       String[] arr = new String[2];
       arr[0] = jwtService.generateAccessToken(user);
       arr[1] = jwtService.generateRefreshToken(user);

       return arr;
    }

    public String refreshToken(String refreshToken)
    {
        Long id = jwtService.getUserIdFromToken(refreshToken);

        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User Not Found with id "+id));

        return jwtService.generateRefreshToken(user);
    }
}
