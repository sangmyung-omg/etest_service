package com.tmax.eTest.Auth.jwt;

import com.tmax.eTest.Auth.dto.PrincipalDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class JwtTokenProvider {
    public static final String tokenSecret = "926D96C90030DD58429D2751AC1BDBBC";
    public static final String tokenExpirationMsec = "86400000";
    public String create(Authentication authentication) {
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        LocalDateTime localDateTime = LocalDateTime.now();
        int sec = Integer.parseInt(tokenExpirationMsec) / 1000;
        localDateTime = localDateTime.plusSeconds(sec);
        ZoneId defaultZoneId = ZoneId.systemDefault();
        Date expireDate = Date.from(localDateTime.atZone(defaultZoneId).toInstant());

        String token = Jwts.builder()
//                .setSubject(Long.(principal.getUserUuid()))
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS256, tokenSecret).compact();
        return token;
    }
    public Claims getClaims(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(tokenSecret)
                .parseClaimsJws(token)
                .getBody();
        return claims;
    }
}