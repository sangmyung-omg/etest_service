package com.tmax.eTest.Auth.controller;

import com.tmax.eTest.Auth.dto.PrincipalDetails;
import com.tmax.eTest.Auth.dto.ResourceNotFoundExceptionDto;
import com.tmax.eTest.Auth.repository.UserRepository;
import com.tmax.eTest.Common.model.user.UserMaster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public String home() {
        return "<h1>Welcome Home</h1>";
    }

    @GetMapping("/user")
    public UserMaster getCurrentUser(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        System.out.println(principalDetails.getId());
        return userRepository.findByEmail(principalDetails.getEmail())
                .orElseThrow(() -> new ResourceNotFoundExceptionDto("User", "id", principalDetails.getEmail()));
    }
}
