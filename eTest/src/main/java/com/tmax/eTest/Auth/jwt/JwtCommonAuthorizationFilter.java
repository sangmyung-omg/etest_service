package com.tmax.eTest.Auth.jwt;


import com.tmax.eTest.Auth.dto.PrincipalDetails;
import com.tmax.eTest.Auth.repository.UserRepository;
import com.tmax.eTest.Common.model.user.UserMaster;
import io.jsonwebtoken.ExpiredJwtException;
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
import java.util.Map;
import java.util.Optional;

public class JwtCommonAuthorizationFilter extends BasicAuthenticationFilter {
    private JwtTokenUtil jwtTokenUtil;

    private UserRepository userRepository;


    public JwtCommonAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository, JwtTokenUtil jwtTokenUtil) {
        super(authenticationManager);
        this.userRepository = userRepository;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
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
        String userUuid = null;
        String token = request.getHeader("Authorization")
                .replace("Bearer ", ""); //Bear 다음에 한칸 뛰어야한다
        if (token != null) {
            try {
                Map<String, Object> parseInfo = jwtTokenUtil.getUserParseInfo(token);
                userUuid = (String) parseInfo.get("userUuid");

            } catch (ExpiredJwtException e) {
                logger.debug("token maybe expired");
            }
            if (userUuid != null) {
                Optional<UserMaster> userMasterOptional = userRepository.findByUserUuid(userUuid);
                UserMaster userMaster = userMasterOptional.get();
                PrincipalDetails principalDetails = PrincipalDetails.create(userMaster);
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());
                return authentication;
            } else {
                logger.info("token maybe expired: userUuid is null.");
            }
        }
        return null;
    }
}