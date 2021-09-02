package com.tmax.eTest.Report.util;

import javax.servlet.http.HttpServletRequest;

import com.tmax.eTest.Auth.jwt.JwtTokenUtil;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class UserIdFetchTool {
    static public String getID(HttpServletRequest request){
        try {
            String authHeader = request.getHeader("Authorization").replace("Bearer ", "");
            if(authHeader == null) {return null;}

            

            
        }
        catch(Exception e){
            log.error("Cannot get userID from request header");
            return null;
        }


        return null;
    }
}
