package com.tmax.eTest.Auth.dto;

import com.tmax.eTest.Test.model.UserMaster;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

public class PrincipalDetails implements UserDetails, OAuth2User {

    private UserMaster userDTO;
    private Map<String, Object> attributes;
    public PrincipalDetails(UserMaster userEntity) {
        this.userDTO = userEntity;
    }
    public PrincipalDetails(UserMaster user, Map<String, Object> attributes) {
        this.userDTO = userDTO;
    }



    @Override
    public String getName() {
        return (String) attributes.get("name");
    }


    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return userDTO.getPassword();
    }

    @Override
    public String getUsername() {
        return userDTO.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
