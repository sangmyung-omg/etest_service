package com.tmax.eTest.Auth.controller;

import com.tmax.eTest.Auth.dto.RefreshTokenRequestDto;
import com.tmax.eTest.Auth.dto.RefreshTokenResponseDto;
import com.tmax.eTest.Auth.jwt.JwtTokenUtil;
import com.tmax.eTest.Auth.repository.UserRepository;
import com.tmax.eTest.Auth.service.PrincipalDetailsService;
import com.tmax.eTest.Auth.service.RefreshTokenService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class RefreshController {
    private static final Logger logger = LoggerFactory.getLogger(RefreshController.class);

    @Autowired
    @Qualifier("AU-UserRepository")
    private UserRepository userRepository;
    @Autowired
    PrincipalDetailsService principalDetailsService;
    @Autowired
    JwtTokenUtil jwtTokenUtil;
    @Autowired
    RefreshTokenService refreshTokenService;

    @PostMapping(path = "/newuser/refresh")
    public RefreshTokenResponseDto refreshAccessToken(@RequestBody RefreshTokenRequestDto refreshTokenRequestDto) {
        return refreshTokenService.refreshAccessToken(refreshTokenRequestDto);
    }
}
