package com.tmax.eTest.Report.util;

import java.util.NoSuchElementException;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import com.tmax.eTest.Auth.jwt.JwtTokenUtil;
import com.tmax.eTest.Auth.repository.UserRepository;
import com.tmax.eTest.Common.model.user.UserMaster;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class UserIdFetchTool {
    @Autowired private JwtTokenUtil jwtTokenUtil;
    @Autowired private UserRepository userRepo;

    public String getID(HttpServletRequest request){
        try {
            String authHeader = request.getHeader("Authorization");
            if(authHeader == null) return null;

            String bearerToken = authHeader.replace("Bearer ", "");
            if(bearerToken == null) {return null;}

            Optional<String> email = Optional.ofNullable(jwtTokenUtil.getEmailFromToken(bearerToken));
            if(!email.isPresent()){return null;}

            Optional<UserMaster> user = userRepo.findByEmail(email.get());

            if(!user.isPresent()){return null;}


            return user.get().getUserUuid();
        }
        catch(NoSuchElementException e){
            log.error("Cannot get userID from request header");
            return null;
        }
    }
}
