package com.tmax.eTest.Auth.service;

import com.tmax.eTest.Auth.dto.CMRespDto;
import com.tmax.eTest.Auth.dto.PrincipalDetails;
import com.tmax.eTest.Auth.dto.RefreshTokenRequestDto;
import com.tmax.eTest.Auth.dto.RefreshTokenResponseDto;
import com.tmax.eTest.Auth.jwt.JwtTokenUtil;
import com.tmax.eTest.Auth.repository.UserRepository;
import com.tmax.eTest.Common.model.user.UserMaster;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class RefreshTokenService {
    @Autowired
    @Qualifier("AU-UserRepository")
    private UserRepository userRepository;
    @Autowired
    PrincipalDetailsService principalDetailsService;
    @Autowired
    JwtTokenUtil jwtTokenUtil;
    public RefreshTokenResponseDto refreshAccessToken(RefreshTokenRequestDto refreshTokenRequestDto) {
        String accessToken = refreshTokenRequestDto.getAccessToken();
        String refreshToken = refreshTokenRequestDto.getRefreshToken();
        String userUuid = null;
        try {
            Map<String, Object> parseInfo = jwtTokenUtil.getUserParseInfo(accessToken);
            userUuid = (String) parseInfo.get("userUuid");
        } catch (ExpiredJwtException e) {
            userUuid = (String) e.getClaims().get("userUuid");
        }
        Optional<UserMaster> userMasterOptional = userRepository.findByUserUuid(userUuid);
        UserMaster userMaster = userMasterOptional.get();
        String refreshTokenFromDb = userMasterOptional.get().getRefreshToken();
        if (refreshToken.equals(refreshTokenFromDb) && !jwtTokenUtil.isTokenExpired(refreshToken)) {
            PrincipalDetails principal = PrincipalDetails.create(userMaster);
            String newToken =  jwtTokenUtil.generateAccessToken(principal);
            return RefreshTokenResponseDto.builder()
                    .accessToken(newToken)
                    .success(true)
                    .build();
        }

        if (jwtTokenUtil.isTokenExpired(refreshToken)) {
            throw new IllegalArgumentException("refreshToken is expired");
        }
        return null;
    }
}
