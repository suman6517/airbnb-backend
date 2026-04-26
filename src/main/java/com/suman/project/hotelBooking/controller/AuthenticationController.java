package com.suman.project.hotelBooking.controller;


import com.suman.project.hotelBooking.dto.LoginDto;
import com.suman.project.hotelBooking.dto.LoginResponseDto;
import com.suman.project.hotelBooking.dto.SignUpReqDto;
import com.suman.project.hotelBooking.dto.UserDto;
import com.suman.project.hotelBooking.secqurity.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController
{
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<UserDto>  signup(@RequestBody SignUpReqDto signUpReqDto)
    {
        return new ResponseEntity<>(authService.signUp(signUpReqDto), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginDto loginDto , HttpServletRequest request , HttpServletResponse response)
    {
        String[] tokens = authService.login(loginDto);

        Cookie cookie = new Cookie("refresh_token", tokens[1]);
        cookie.setHttpOnly(true);

        response.addCookie(cookie);
        return ResponseEntity.ok(new LoginResponseDto(tokens[0]));

    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDto>  refresh(HttpServletRequest request)
    {
        String refreshToken = Arrays.stream(request.getCookies()).
                filter(cookie -> "refresh_token".equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(() -> new AuthenticationServiceException("Refresh token not found inside the Cookies"));

        String accessToken = authService.refreshToken(refreshToken);
        return ResponseEntity.ok(new LoginResponseDto(accessToken));
    }
}
