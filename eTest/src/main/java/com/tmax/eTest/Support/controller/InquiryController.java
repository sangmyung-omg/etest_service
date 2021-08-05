package com.tmax.eTest.Support.controller;

import com.tmax.eTest.Auth.repository.UserRepository;
import com.tmax.eTest.Common.model.support.Inquiry;
import com.tmax.eTest.Common.model.support.Inquiry_file;
import com.tmax.eTest.Common.model.user.UserMaster;
import com.tmax.eTest.Support.Dto.CreateInquiryDto;
import com.tmax.eTest.Support.repository.InquiryFileRepository;
import com.tmax.eTest.Support.repository.InquiryRepository;
import com.tmax.eTest.Support.service.InquiryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
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
