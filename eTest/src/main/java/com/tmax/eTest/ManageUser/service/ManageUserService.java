package com.tmax.eTest.ManageUser.service;

import com.tmax.eTest.Common.model.user.UserMaster;
import com.tmax.eTest.Common.repository.user.UserMasterRepo;
import com.tmax.eTest.ManageUser.model.dto.UserPopupDTO;
import com.tmax.eTest.ManageUser.repository.UserRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ManageUserService {

    protected final Log LOGGER = LogFactory.getLog(getClass());

    @Autowired
    UserRepository userRepository;

    public UserPopupDTO getUserPopupData(String user_uuid){
        UserMaster user = userRepository.findByUserUuid(user_uuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No User Found With Provided UUID"));

        UserPopupDTO userPopupDTO = UserPopupDTO.builder()
                .user_uuid(user.getUserUuid())
                .nick_name(user.getNickname())
                .gender(user.getGender())
                .email(user.getEmail())
                .birthday(user.getBirthday())
                .build();

        return userPopupDTO;
    }

}
