package com.tmax.eTest.Auth.dto;

import com.tmax.eTest.Common.model.user.UserMaster;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.*;

public class PrincipalDetails implements OAuth2User, UserDetails {

    private static final long serialVersionUID = 1L;

    private String email;
    private String userUuid;
    private Collection<? extends GrantedAuthority> authorities;
    private Map<String, Object> attributes;
    private String nickname;
    private String name;
    public PrincipalDetails( String name, String email, String userUuid, String nickname, Collection<? extends GrantedAuthority> authorities) {
        this.name = name;
        this.email = email;
        this.authorities = authorities;
        this.userUuid = userUuid;
        this.nickname = nickname;
    }

    public static PrincipalDetails create(UserMaster user) {
        List<GrantedAuthority> authorities = Collections.
                singletonList(new SimpleGrantedAuthority("ROLE_"+user.getRole()));

        return new PrincipalDetails(
                user.getName(),
                user.getEmail(),
                user.getUserUuid(),
                user.getNickname(),
                authorities
        );
    }

    public static PrincipalDetails create(UserMaster user, Map<String, Object> attributes) {
        PrincipalDetails userPrincipal = PrincipalDetails.create(user);
        userPrincipal.setAttributes(attributes);
        return userPrincipal;
    }


    public String getEmail() {
        return email;
    }


    @Override
    public String getUsername() {
        return email;
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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getName() {
        return name;
    }

    public String getUserUuid() { return userUuid;}

    public String getNickname() {
        return nickname;
    }
}