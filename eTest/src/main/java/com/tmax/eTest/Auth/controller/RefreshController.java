package com.tmax.eTest.Auth.controller;

import com.tmax.eTest.Auth.dto.PrincipalDetails;
import com.tmax.eTest.Auth.jwt.JwtTokenUtil;
import com.tmax.eTest.Auth.repository.UserRepository;
import com.tmax.eTest.Auth.service.PrincipalDetailsService;
import com.tmax.eTest.Common.model.user.UserMaster;
import com.tmax.eTest.Support.service.InquiryService;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

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

    @PostMapping(path="/newuser/refresh")
    public Map<String, Object> requestForNewAccessToken(@RequestBody Map<String, String> m) {
        String accessToken = null;
        String refreshToken = null;
        String refreshTokenFromDb = null;
        String userUuid = null;
        Map<String, Object> map = new HashMap<>();

        try {
            accessToken = m.get("accessToken");
            refreshToken = m.get("refreshToken");
            try {
                Map<String, Object> parseInfo = jwtTokenUtil.getUserParseInfo(accessToken);
                userUuid = (String)parseInfo.get("userUuid");
            } catch (IllegalArgumentException e) {
                log.info("userUuid parsing fail");
            }  catch (ExpiredJwtException e) {
                userUuid = (String) e.getClaims().get("userUuid");
                log.info("expired access token" );
            }  catch (NoSuchElementException e) {
                log.info("userUuid not exist");
            }
            if (refreshToken != null) { //refresh를 같이 보냈으면.
                try {
                    Optional<UserMaster> userMasterOptional = userRepository.findByUserUuid(userUuid);
                    UserMaster userMaster = userMasterOptional.get();
                    refreshTokenFromDb = userMasterOptional.get().getRefreshToken();
                } catch (IllegalArgumentException e) {
                    log.warn("illegal argument");
                }
                //둘이 일치하고 만료도 안됐으면 재발급 해주기.
                if (refreshToken.equals(refreshTokenFromDb) && !jwtTokenUtil.isTokenExpired(refreshToken)) {
                    Optional<UserMaster> userMasterOptional  = userRepository.findByUserUuid(userUuid);
                    UserMaster userMaster = userMasterOptional.get();
                    PrincipalDetails principal = PrincipalDetails.create(userMaster);
                    String newToken =  jwtTokenUtil.generateAccessToken(principal);
                    map.put("success", true);
                    map.put("accessToken", newToken);
                } else {
                    map.put("success", false);
                    map.put("msg", "refresh token is expired.");
                }
            } else { //refresh token이 없으면
                map.put("success", false);
                map.put("msg", "your refresh token does not exist.");
            }
        } catch (NullPointerException e) {
            logger.debug("access token이나 refresh token이 제대로 안들어옴");
        }
        return map;
    }
}
