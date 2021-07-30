package com.tmax.eTest.Auth.model;

import com.tmax.eTest.Auth.exception.OAuth2AuthenticationProcessingException;

import java.util.Map;


public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        System.out.println("OAuth2UserInfoFactory의 attributes" + attributes.get("kakao_account"));
        System.out.println("OAuth2UserInfoFactory의 attributes" + attributes);

        if(registrationId.equalsIgnoreCase(AuthProvider.google.name())) {
            return new GoogleOAuth2UserInfo(attributes);
        } else if (registrationId.equalsIgnoreCase(AuthProvider.facebook.name())) {
            return new FacebookOAuth2UserInfo(attributes);
        } else if (registrationId.equalsIgnoreCase(AuthProvider.naver.name())) {
            return new NaverOAuth2UserInfo((Map)attributes.get("response"));
        } else if (registrationId.equalsIgnoreCase(AuthProvider.kakao.name())) {
            Map attributes_temp = (Map)attributes.get("kakao_account");
            attributes_temp.put("id",attributes.get("id"));
            return new KakaoOAuth2UserInfo(attributes_temp);
        } else {
            throw new OAuth2AuthenticationProcessingException("Sorry! Login with " + registrationId + " is not supported yet.");
        }
    }
}