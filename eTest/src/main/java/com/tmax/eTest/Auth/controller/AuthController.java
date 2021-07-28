package com.tmax.eTest.Auth.controller;

import com.tmax.eTest.Auth.dto.*;
import com.tmax.eTest.Auth.jwt.JwtTokenProvider;
import com.tmax.eTest.Auth.model.AuthProvider;
import com.tmax.eTest.Auth.repository.UserRepository;
import com.tmax.eTest.Common.model.user.UserMaster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequestDto loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = tokenProvider.create(authentication);
        return ResponseEntity.ok(new AuthResponseDto(token));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequestDto signUpRequest) {
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new BadRequestExceptionDto("Email address already in use.");
        }
        // Creating user's account
        UserMaster user = new UserMaster();
        user.setName(signUpRequest.getName());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(signUpRequest.getPassword());
        user.setProvider(AuthProvider.local);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        UserMaster result = userRepository.save(user);

        URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/me")
                .buildAndExpand(result.getId()).toUri();

        return ResponseEntity.created(location).body(new ApiResponseDto(true, "User registered successfully@"));
    }

    @GetMapping("/success")
    public ResponseEntity<?> signinSuccess() {
        return ResponseEntity.ok(new ApiResponseDto(true, "JWT 발행 성공"));
    }

}