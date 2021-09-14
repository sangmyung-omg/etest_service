package com.tmax.eTest.Contents.util;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.tmax.eTest.Auth.jwt.JwtTokenUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component(value = "JWTUtils")
public class JWTUtils {

  @Autowired
  private JwtTokenUtil jwtTokenUtil;

  @Autowired
  private CommonUtils commonUtils;

  public String getUserId(HttpServletRequest request) {
    String header = request.getHeader("Authorization");
    String userId = null;
    if (commonUtils.stringNullCheck(header)) {
      log.info("header is null : Unregistered User.");
    } else {
      log.info("header exists : Registered User.");
      String token = header.replace("Bearer ", "");
      Map<String, Object> parseInfo = jwtTokenUtil.getUserParseInfo(token);
      userId = parseInfo.get("userUuid").toString();
      log.info("User ID : " + userId);
    }
    return userId;
  }
}
