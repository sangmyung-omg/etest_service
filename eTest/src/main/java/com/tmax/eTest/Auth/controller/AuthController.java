package com.tmax.eTest.Auth.controller;

import com.tmax.eTest.Auth.dto.*;

import com.tmax.eTest.Auth.jwt.JwtTokenUtil;
import com.tmax.eTest.Auth.repository.UserRepository;
import com.tmax.eTest.Auth.service.AuthService;
import com.tmax.eTest.Common.model.user.UserMaster;
import com.tmax.eTest.LRS.dto.StatementDTO;
import com.tmax.eTest.LRS.util.LRSAPIManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

@RestController
@RequiredArgsConstructor
public class AuthController {
    @Autowired
    LRSAPIManager lrsapiManager;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    @Qualifier("AU-UserRepository")
    private UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthService authService;

    public static String getClientIp(HttpServletRequest req) {
        String ip = req.getHeader("X-Forwarded-For");
        if (ip == null) ip = req.getRemoteAddr();
        return ip;
    }
    @PostMapping("/login")
    public CMRespDto<?> jwtCreate(@RequestBody Map<String, Object> data, HttpServletRequest httpServletRequest) {
        String ip = getClientIp(httpServletRequest);
        return authService.login((String) data.get("providerId"), AuthProvider.valueOf((String) data.get("provider")), ip );
    }

    @PostMapping("/signup")
    public CMRespDto<?> signup(@RequestBody SignUpRequestDto signUpRequestDto){
        return authService.singUp(signUpRequestDto);
    }

    @PostMapping("/emailDuplicateCheck")
    public CMRespDto<?> duplicateCheck(@RequestBody DuplicateCheckDto duplicateCheckDto) {
        Map<String,String> data = new HashMap<>();
        Boolean res = false;
        if ( authService.emailDuplicateCheck(duplicateCheckDto.getEmail()) ){
            data.put("email", "중복된 회원이 존재합니다");
            res = true;
        }else{
            data.put("email","중복된 회원 없음");
        }
        if (res == true) {
            return new CMRespDto<>(201, "중복 회원 존재", data);
        } else{
            return new CMRespDto<>(200, "회원가입 가능", data);
        }
    }

    @PostMapping("/nicknameDuplicateCheck")
    public CMRespDto<?> nicknameDuplicateCheck(@RequestBody DuplicateCheckDto duplicateCheckDto) {
        Map<String,String> data = new HashMap<>();
        Boolean res = false;
        if ( authService.nickNameDuplicateCheck(duplicateCheckDto.getNickname()) ){
            data.put("nickname","중복된 회원이 존재합니다");
            res = true;
        }else{
            data.put("nickname","중복된 회원 없음");
        }
        if (res == true) {
            return new CMRespDto<>(201, "중복 회원 존재", data);
        } else{
            return new CMRespDto<>(200, "회원가입 가능", data);
        }
    }


    @GetMapping("/user/delete")
    public CMRespDto<?> deleteUser(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        String result = authService.deleteUser(principalDetails);
        if (result == "True") {
            return new CMRespDto<>(200,"성공",result);
        }
        return new CMRespDto<>(400,"실패",result);
    }

    @GetMapping("/user/logout")
    public CMRespDto<?> logout(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        return authService.logout(principalDetails);
    }
}


