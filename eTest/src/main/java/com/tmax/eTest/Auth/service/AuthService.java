package com.tmax.eTest.Auth.service;

import com.tmax.eTest.Auth.dto.*;
import com.tmax.eTest.Auth.repository.UserRepository;
import com.tmax.eTest.Common.model.user.UserMaster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.Optional;
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
            if (!emailDuplicateCheck(signUpRequestDto.getEmail()) && !nickNameDuplicateCheck(signUpRequestDto.getNickname())) {
                UserMaster userMaster = UserMaster.builder()
                        .nickname(signUpRequestDto.getNickname())
                        .email(signUpRequestDto.getEmail())
                        .provider(signUpRequestDto.getProvider())
                        .role(Role.USER)
                        .name(signUpRequestDto.getName())
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
        return null;
    }

    @Transactional(readOnly = true)
    public boolean emailDuplicateCheck(String email) {
        return userRepository.existsByEmail(email);
    }

    @Transactional(readOnly = true)
    public boolean nickNameDuplicateCheck(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    @Transactional
    public String deleteUser(String userUuid) {
        Optional<UserMaster> userMasterOptional = userRepository.findByUserUuid(userUuid);
        userRepository.delete(userMasterOptional.get());
        return "True";
    }

    @Transactional
    public CMRespDto<?> modifyUserInfo(PrincipalDetails principalDetails, ModifyUserInfoDto modifyUserInfoDto){
        Optional<UserMaster> userMasterOptional = userRepository.findByUserUuid(principalDetails.getUserUuid());
        UserMaster userMaster = userMasterOptional.get();
        userMaster.setNickname(modifyUserInfoDto.getNickname());
        userMaster.setBirthday(modifyUserInfoDto.getBirthday());
        userMaster.setEmail(modifyUserInfoDto.getEmail());
        userMaster.setGender(modifyUserInfoDto.getGender());
        return new CMRespDto<>(200,"회원정보 수정 성공","성공");
    };

}
