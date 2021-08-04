package com.tmax.eTest.Auth.service;

import com.tmax.eTest.Auth.dto.SignUpRequestDto;
import com.tmax.eTest.Auth.dto.Role;
import com.tmax.eTest.Auth.repository.UserRepository;
import com.tmax.eTest.Common.model.user.UserMaster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class AuthService {

    @Autowired
    @Qualifier("AU-UserRepository")
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public UserMaster singUp(SignUpRequestDto signUpRequestDto) {
        UserMaster userMaster = UserMaster.builder()
                .password(bCryptPasswordEncoder.encode(UUID.randomUUID().toString()))
                .email(signUpRequestDto.getEmail())
                .provider(signUpRequestDto.getProvider())
                .role(Role.USER)
                .userUuid(UUID.randomUUID().toString())
                .gender(signUpRequestDto.getGender())
                .providerId(signUpRequestDto.getProviderId())
                .birthday(signUpRequestDto.getBirthday())
                .event_sms_agreement(signUpRequestDto.getEvent_sms_agreement())
                .account_active(signUpRequestDto.getAccount_active())
                .older_than_14(true)
                .service_agreement(true)
                .collect_info(true)
                .build();
        userRepository.save(userMaster);
        return userMaster;
    }


}
