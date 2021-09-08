package com.tmax.eTest.Auth.controller;

import com.tmax.eTest.Auth.dto.*;
import com.tmax.eTest.Auth.jwt.JwtTokenUtil;
import com.tmax.eTest.Auth.jwt.RefreshToken;
import com.tmax.eTest.Auth.repository.UserRepository;
import com.tmax.eTest.Auth.service.AuthService;
import com.tmax.eTest.Common.model.user.UserMaster;
import com.tmax.eTest.LRS.dto.StatementDTO;
import com.tmax.eTest.LRS.util.LRSAPIManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

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
    RedisTemplate<String, Object> redisTemplate;

    @Value("${jwt.secret}")
    private String secret;

    @Autowired
    @Qualifier("AU-UserRepository")
    private UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthService authService;
    @Value("${jwt.access.token.time}")
    private long JWT_ACCESS_TOKEN_VALIDITY;
    @Value("${jwt.refresh.token.time}")
    private long JWT_REFRESH_TOKEN_VALIDITY;

    @PostMapping("/login")
    public CMRespDto<?> jwtCreate(@RequestBody Map<String, Object> data) {
        Optional<UserMaster> userMasterOptional =
                userRepository.findByProviderIdAndProvider((String) data.get("providerId"),AuthProvider.valueOf((String) data.get("provider")));
        if (userMasterOptional.isPresent()) {
            UserMaster userMaster = userMasterOptional.get();
            PrincipalDetails principal = PrincipalDetails.create(userMaster);

            String jwtToken = jwtTokenUtil.generateAccessToken(principal);
            String refreshToken = jwtTokenUtil.generateRefreshToken(userMaster.getEmail());

            RefreshToken refreshTokenEntity = new RefreshToken();
            refreshTokenEntity.setEmail(userMaster.getEmail());
            refreshTokenEntity.setRefreshToken(refreshToken);
            ValueOperations<String, Object> vop = redisTemplate.opsForValue();
            vop.set(userMaster.getEmail(), refreshTokenEntity,JWT_REFRESH_TOKEN_VALIDITY, TimeUnit.SECONDS);

            Map<String,String> info = new HashMap<>();
            info.put("jwtToken",jwtToken);
            info.put("email",userMaster.getEmail());
            info.put("gender", userMaster.getGender().toString());
            info.put("birthday", userMaster.getBirthday().toString());
            info.put("nickname",userMaster.getNickname());
            info.put("provider",userMaster.getProvider().toString());
            info.put("providerId",userMaster.getProviderId().toString());
            info.put("refreshToken", refreshToken);

            // LRS에 로그인 정보 저장
            StatementDTO statementDTO = new StatementDTO();
            statementDTO.setUserId(userMaster.getUserUuid());
            statementDTO.setActionType("enter");
            statementDTO.setSourceType("application");
            Date date = new Date(System.currentTimeMillis());
            SimpleDateFormat sdf;
            sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
            sdf.setTimeZone(TimeZone.getTimeZone("KST"));
            String text = sdf.format(date);
            statementDTO.setTimestamp(text);
            List<StatementDTO> statementDTOList = new ArrayList<>();
            statementDTOList.add(statementDTO);
            try {
                lrsapiManager.saveStatementList(statementDTOList);
            } catch (ParseException e) {
            }


            return new CMRespDto<>(200, "jwt 반환", info);
        }
        return new CMRespDto<>(201, "회원 가입이 안된 유저",null);
    }

    @PostMapping("/signup")
    @Transactional
    public CMRespDto<?> signup(@RequestBody SignUpRequestDto signUpRequestDto){
        UserMaster userMaster = authService.singUp(signUpRequestDto);
        if (userMaster == null) {
            return new CMRespDto<> (201 , "회원가입 불가능","이메일이나 닉네임 중복 회원이 존재합니다.");
        }
        PrincipalDetails principal = PrincipalDetails.create(userMaster);
        String jwtToken = jwtTokenUtil.generateAccessToken(principal);
        return new CMRespDto<> (200 , "회원가입 완료",jwtToken);
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
    @Transactional
    public CMRespDto<?> deleteUser(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        String result = authService.deleteUser(principalDetails.getUserUuid());
        if (result == "True") {
            return new CMRespDto<>(200,"성공",result);
        }
        return new CMRespDto<>(400,"실패",result);
    }

    @GetMapping("/user/logout")
    @Transactional
    public CMRespDto<?> logout(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        // redis refresh token 삭제
        ValueOperations<String, Object> vop = redisTemplate.opsForValue();
        vop.set(principalDetails.getEmail(), 0,1, TimeUnit.NANOSECONDS);

        // LRS에 로그아웃 정보 저장
        StatementDTO statementDTO = new StatementDTO();
        statementDTO.setUserId(principalDetails.getUserUuid());
        statementDTO.setActionType("quit");
        statementDTO.setSourceType("application");
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf;
        sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        sdf.setTimeZone(TimeZone.getTimeZone("KST"));
        String text = sdf.format(date);
        statementDTO.setTimestamp(text);
        List<StatementDTO> statementDTOList = new ArrayList<>();
        statementDTOList.add(statementDTO);
        try {
            lrsapiManager.saveStatementList(statementDTOList);
        } catch (ParseException e) {
        }
        return new CMRespDto<>(200,"로그아웃 성공",text);
    }
}


