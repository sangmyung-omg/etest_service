package com.tmax.eTest.Auth.dto;

import com.tmax.eTest.Common.model.user.UserMaster;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Data
public class PrincipalDetails implements UserDetails, OAuth2User{

    private static final long serialVersionUID = 1L;

    private UserMaster user;
    private Map<String, Object> attributes;

    public PrincipalDetails(UserMaster user) {
        this.user = user;
    }

    public PrincipalDetails(UserMaster user, Map<String, Object> attributes) {
        this.user = user;
        System.out.println(user);

    }
    // 권한 : 한개가 아닐 수 있음. (3개 이상의 권한)
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    public String getUserType() {
        return user.getUserType();
    }

    public String getUserUuid() {
        return user.getUserUuid();
    }

    public String getEmail() {
        return user.getEmail();
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
    public Map<String, Object> getAttributes() {
        return attributes;  // {id:343434343, name:이민준, email:minjoon1995@naver.com}
    }
    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return (String) attributes.get("name");
    }


}

