package com.tmax.eTest.Support.controller;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tmax.eTest.Auth.dto.CMRespDto;
import com.tmax.eTest.Auth.repository.UserRepository;
import com.tmax.eTest.Common.model.support.Inquiry;
import com.tmax.eTest.Common.model.support.Inquiry_file;
import com.tmax.eTest.Common.model.user.UserMaster;
import com.tmax.eTest.Support.Dto.CreateInquiryDto;
import com.tmax.eTest.Support.repository.InquiryFileRepository;
import com.tmax.eTest.Support.repository.InquiryRepository;
import com.tmax.eTest.Support.service.InquiryService;
import io.swagger.models.auth.In;
import jdk.internal.org.objectweb.asm.TypeReference;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.Table;
import java.util.*;

@RestController
public class InquiryController {

    @Autowired
    InquiryService inquiryService;

    @Autowired
    @Qualifier("SU-InquiryRepository")
    InquiryRepository inquiryRepository;

    @Autowired
    @Qualifier("AU-UserRepository")
    UserRepository userRepository;

    @Autowired
    @Qualifier("SU-InquiryFileRepository")
    InquiryFileRepository inquiryFileRepository;

    @PostMapping("/Inquiry/create")
    @Transactional
    public void Inquiry(@RequestBody CreateInquiryDto createInquiryDto) {
        System.out.println("create inquiry 진입");
        Optional<UserMaster> userMaster_temp = userRepository.findByEmail("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        UserMaster userMaster_temp_1 = userMaster_temp.get();
        Inquiry inquiry =
                Inquiry.builder()
                .userMaster(userMaster_temp_1)
                .content(createInquiryDto.getContent())
                .status(createInquiryDto.getStatus())
                .date(createInquiryDto.getDate())
                .title(createInquiryDto.getTitle())
                .type(createInquiryDto.getType())
                .inquiry_file(createInquiryDto.getInquiry_file())
                        .build();
        inquiryRepository.save(inquiry);
        for(int i=0; i<createInquiryDto.getInquiry_file().size(); i++){
            Inquiry_file inquiry_file = Inquiry_file.builder()
                    .url(createInquiryDto.getInquiry_file().get(i).getUrl())
                    .type(createInquiryDto.getInquiry_file().get(i).getType())
                    .inquiry(inquiry)
                    .build();
            inquiryFileRepository.save(inquiry_file);
        }
    }
}
