package com.tmax.eTest.Auth.model;

import java.util.Map;

public class KakaoOAuth2UserInfo extends OAuth2UserInfo{

    public KakaoOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return (String) attributes.get("id");
    }
    @Override
    public String getName() {
        return (String)attributes.get("name");
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public String getEmail() {
        return (String)attributes.get("email");
    }

    @Override
    public String getImageUrl() {
        return null;
    }
}
