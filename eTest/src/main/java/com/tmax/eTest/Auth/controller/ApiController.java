package com.tmax.eTest.Auth.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.tmax.eTest.Auth.dto.*;
import com.tmax.eTest.Auth.jwt.JwtProperties;
import com.tmax.eTest.Auth.repository.UserRepository;
import com.tmax.eTest.Auth.service.AuthService;
import com.tmax.eTest.Common.model.user.UserMaster;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class ApiController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthService authService;

    @PostMapping("/login")
    public CMRespDto<?> jwtCreate(@RequestBody Map<String, Object> data) {
        System.out.println("로그인 시도");
        Optional<UserMaster> userEntity =
                userRepository.findByProviderIdAndProvider((String) data.get("providerId"),AuthProvider.valueOf((String) data.get("provider")));
        if (userEntity.isPresent()) {
            System.out.println("기존에 로그인 했던 유저입니다");
            String jwtToken = JWT.create()
                    .withSubject(userEntity.get().getEmail())
                    .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME))
                    .withClaim("userUuid", userEntity.get().getUserUuid())
                    .withClaim("email", userEntity.get().getEmail())
                    .sign(Algorithm.HMAC512(JwtProperties.SECRET));
            return new CMRespDto<>(200, "jwt 반환", jwtToken);
        }

        System.out.println("처음 로그인 하는 유저입니다");
        return new CMRespDto<>(201, "회원 가입이 안된 유저",null);
    }
    @PostMapping("/signup")
    @Transactional
    public CMRespDto<?> signup(@RequestBody SignUpRequestDto signUpRequestDto){
        UserMaster userMaster = authService.singUp(signUpRequestDto);
        String jwtToken = JWT.create()
                .withSubject(userMaster.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis()+ JwtProperties.EXPIRATION_TIME))
                .withClaim("userUuid", userMaster.getUserUuid())
                .withClaim("email", userMaster.getEmail())
                .sign(Algorithm.HMAC512(JwtProperties.SECRET));
        return new CMRespDto<> (200 , "회원가입 완료",jwtToken);
    }

    @PostMapping("/duplicateCheck")
    public CMRespDto<?> duplicateCheck(@RequestBody DuplicateCheckDto duplicateCheckDto) {
        String email = duplicateCheckDto.getEmail();
        Boolean res = userRepository.existsByEmail(email);
        if (res == true) {
            return new CMRespDto<>(201, "중복 회원 존재", true);
        } else{
            return new CMRespDto<>(200, "회원가입 가능", false);
        }
    }
}


