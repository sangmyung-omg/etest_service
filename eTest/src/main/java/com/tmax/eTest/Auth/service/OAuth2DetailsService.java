package com.tmax.eTest.Auth.service;

import com.tmax.eTest.Auth.dto.PrincipalDetails;
import com.tmax.eTest.Auth.exception.OAuth2AuthenticationProcessingException;
import com.tmax.eTest.Auth.model.*;
import com.tmax.eTest.Common.model.user.UserMaster;
import com.tmax.eTest.Auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class OAuth2DetailsService extends DefaultOAuth2UserService {
    @Autowired
    @Qualifier("AU-UserRepository")
    private UserRepository userRepository;

    @Transactional
    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        System.out.println("OAuth2DetailsService의 loadUser 실행");
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

        try {
            return processOAuth2User(oAuth2UserRequest, oAuth2User);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            // Throwing an instance of AuthenticationException will trigger the OAuth2AuthenticationFailureHandler
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }
    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        System.out.println("OAuth2DetailsService의 processOAuth2User 실행");

        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(oAuth2UserRequest.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes());
        if(StringUtils.isEmpty(oAuth2UserInfo.getEmail())) {
            throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
        }
        Optional<UserMaster> userOptional = userRepository.findByEmail(oAuth2UserInfo.getEmail());
        System.out.println("oAuth2UserInfo : " + oAuth2UserInfo);
        UserMaster user;

        if(userOptional.isPresent()) {

            user = userOptional.get();
            System.out.println("user : "+ user);
            System.out.println("user.getProvider() : " +user.getProvider());
            System.out.println(oAuth2UserRequest.getClientRegistration().getRegistrationId());
            System.out.println(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()));

            if(!user.getProvider().equals(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()))) {

                throw new OAuth2AuthenticationProcessingException("Looks like you're signed up with " +
                        user.getProvider() + " account. Please use your " + user.getProvider() +
                        " account to login.");

            }

            user = updateExistingUser(user, oAuth2UserInfo);
        } else {
            user = registerNewUser(oAuth2UserRequest, oAuth2UserInfo);
        }
        return PrincipalDetails.create(user, oAuth2User.getAttributes());
    }
    private UserMaster registerNewUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {
        System.out.println("OAuth2DetailsService의 registerNewUser 실행");

        UserMaster user = new UserMaster();
        System.out.println("OAuth2DetailsService의" + oAuth2UserInfo);
        user.setProvider(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()));
        user.setProviderId(oAuth2UserInfo.getId());
        user.setName(oAuth2UserInfo.getName());
        user.setEmail(oAuth2UserInfo.getEmail());
        user.setRole(Role.USER);
        user.setUserUuid(UUID.randomUUID().toString()); // 추가
        return userRepository.save(user);
    }

    private UserMaster updateExistingUser(UserMaster existingUser, OAuth2UserInfo oAuth2UserInfo) {
        System.out.println("OAuth2DetailsService의 updateExistingUser 실행");
        existingUser.setName(oAuth2UserInfo.getName());
        return userRepository.save(existingUser);
    }
}
