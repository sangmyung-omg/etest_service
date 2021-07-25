package com.tmax.eTest.Auth.service;

import com.tmax.eTest.Auth.dto.PrincipalDetails;
import com.tmax.eTest.Test.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import com.tmax.eTest.Test.model.UserMaster;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class OAuth2DetailsService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("OAuth2 서비스 탐");
        OAuth2User oauth2User = super.loadUser(userRequest);
        System.out.println(oauth2User.getAttributes());

        Map<String, Object> userInfo = oauth2User.getAttributes();

        String password = new BCryptPasswordEncoder().encode(UUID.randomUUID().toString());
        String email = (String) userInfo.get("email");
        String name = (String) userInfo.get("name");

        UserMaster userEntity = userRepository.findByEmail(email);

        if(userEntity == null) { // 페이스북 최초 로그인
            UserMaster user = UserMaster.builder()
                    .password(password)
                    .email(email)
                    .name(name)
                    .build();

            return new PrincipalDetails(userRepository.save(user), oauth2User.getAttributes());
        }else { // 페이스북으로 이미 회원가입이 되어 있다는 뜻
            return new PrincipalDetails(userEntity, oauth2User.getAttributes());
        }

    }
}
