package com.tmax.eTest.Auth.jwt;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.tmax.eTest.Auth.dto.PrincipalDetails;
import com.tmax.eTest.Auth.model.JwtProperties;
import com.tmax.eTest.Auth.repository.UserRepository;
import com.tmax.eTest.Common.model.user.UserMaster;
import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public class JwtCommonAuthorizationFilter extends BasicAuthenticationFilter {

    private UserRepository userRepository;
    private JwtTokenProvider jwtTokenProvider;


    public JwtCommonAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
        super(authenticationManager);
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("JwtCommonAuthorizationFilter의 dofilter탐");
        // Read the Authorization header, where the JWT token should be
        String header = request.getHeader("Authorization");

        // If header does not contain BEARER or is null delegate to Spring impl and exit
        if (header == null || !header.startsWith("Bearer")) {
            chain.doFilter(request, response);
            return;
        }
        // If header is present, try grab user principal from database and perform authorization
        Authentication authentication = getUsernamePasswordAuthentication(request);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // Continue filter execution
        chain.doFilter(request, response);
    }

    private Authentication getUsernamePasswordAuthentication(HttpServletRequest request) {
        System.out.println("JwtCommonAuthorizationFilter의 getUsernamePasswordAuthentication탐");
        String token = request.getHeader("Authorization")
                .replace("Bearer ",""); //Bear 다음에 한칸 뛰어야한다.. 개고생하지말자
        System.out.println("token :" +token);
        if (token != null) {
            String claims = request.getHeader(JwtProperties.HEADER_STRING).replace(JwtProperties.TOKEN_PREFIX, "");
            String email = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(token)
                    .getClaim("email").asString();
            if (email != null) {
                Optional<UserMaster> oUser = userRepository.findByEmail(email);
                UserMaster user = oUser.get();
                PrincipalDetails principal = PrincipalDetails.create(user);

                // OAuth 인지 일반 로그인인지 구분할 필요가 없음. 왜냐하면 password를 Authentication이 가질 필요가 없으니!!
                // JWT가 로그인 프로세스를 가로채서 인증다 해버림. (OAuth2.0이든 그냥 일반 로그인 이든)

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication); // 세션에 넣기
                return authentication;
            }
        }
        return null;
    }

}