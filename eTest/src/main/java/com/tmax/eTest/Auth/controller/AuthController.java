package com.tmax.eTest.Auth.controller;

import com.tmax.eTest.Auth.dto.*;
import com.tmax.eTest.Auth.jwt.JwtTokenUtil;
import com.tmax.eTest.Auth.model.PrincipalDetails;
import com.tmax.eTest.Auth.repository.UserRepository;
import com.tmax.eTest.Auth.service.AuthService;
import com.tmax.eTest.LRS.util.LRSAPIManager;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    LRSAPIManager lrsapiManager;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    @Qualifier("AU-UserRepository")
    private UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthService authService;

    @PostMapping("/login")
    public CMRespDto<?> jwtCreate(@RequestBody LoginRequestDto loginRequestDTO) {
        return authService.login(loginRequestDTO);
    }

    @PostMapping("/signup")
    public CMRespDto<?> signup(@RequestBody SignUpRequestDto signUpRequestDto) {
        return authService.singUp(signUpRequestDto);
    }

    @PostMapping("/emailDuplicateCheck")
    public CMRespDto<?> duplicateCheck(@RequestBody EmailDuplicateCheckReqDto emailDuplicateCheckReqDto) {
        return authService.emailDuplicateCheck(emailDuplicateCheckReqDto);
    }

    @PostMapping("/nicknameDuplicateCheck")
    public CMRespDto<?> nicknameDuplicateCheck(@RequestBody NicknameDuplicateCheckReqDto nicknameDuplicateCheckReqDto) {
        return authService.nickNameDuplicateCheck(nicknameDuplicateCheckReqDto);
    }


    @GetMapping("/user/delete")
    public CMRespDto<?> deleteUser(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        String result = authService.deleteUser(principalDetails);
        if (result == "True") {
            return new CMRespDto<>(200, "성공", result);
        }
        return new CMRespDto<>(400, "실패", result);
    }

    @GetMapping("/user/logout")
    public CMRespDto<?> logout(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        return authService.logout(principalDetails);
    }

    @PostMapping("/user/modifyUserInfo")
    @Transactional
    public CMRespDto<?> modifyUserInfo(@AuthenticationPrincipal PrincipalDetails principalDetails, @RequestBody ModifyUserInfoDto modifyUserInfoDto) {
        authService.modifyUserInfo(principalDetails, modifyUserInfoDto);
        return new CMRespDto<>(200, "성공", "성공");
    }
}