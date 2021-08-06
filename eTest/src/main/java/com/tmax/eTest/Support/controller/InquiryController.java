package com.tmax.eTest.Support.controller;

import com.tmax.eTest.Auth.dto.CMRespDto;
import com.tmax.eTest.Auth.dto.PrincipalDetails;
import com.tmax.eTest.Auth.repository.UserRepository;
import com.tmax.eTest.Common.model.support.Inquiry;
import com.tmax.eTest.Common.model.support.Inquiry_file;
import com.tmax.eTest.Common.model.user.UserMaster;
import com.tmax.eTest.Support.dto.CreateInquiryDto;
import com.tmax.eTest.Support.repository.InquiryFileRepository;
import com.tmax.eTest.Support.repository.InquiryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;

@RestController
@RequestMapping("/user")
public class InquiryController {

//    @Autowired
//    InquiryService inquiryService;

    @Autowired
    @Qualifier("SU-InquiryRepository")
    InquiryRepository inquiryRepository;

    @Autowired
    @Qualifier("AU-UserRepository")
    UserRepository userRepository;

    @Autowired
    @Qualifier("SU-InquiryFileRepository")
    InquiryFileRepository inquiryFileRepository;

    @PostMapping("/inquiry/create")
    @Transactional
    public CMRespDto<?> Inquiry(@AuthenticationPrincipal PrincipalDetails principalDetails, @RequestBody CreateInquiryDto createInquiryDto) {
        System.out.println("create inquiry 진입");
        Optional<UserMaster> userMaster_temp = userRepository.findByEmail(principalDetails.getEmail());
        UserMaster userMasterEntity= userMaster_temp.get();
        Inquiry inquiry =
                Inquiry.builder()
                .userMaster(userMasterEntity)
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
        return new CMRespDto<>(200,"1대1 질문 생성 성공",inquiry.getId());
    }

    @Transactional(readOnly = true)
    @PostMapping("/inquiry/list")
    public CMRespDto<?> getInquiryList(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        List<Inquiry> inquiryList = inquiryRepository.findAllByUserUuid(principalDetails.getUserUuid());
//        Optional<Inquiry> inquiryId = inquiryRepository.findById(6L);
        return new CMRespDto<>(200,"1대1 질문 리스트 받아오기 성공",inquiryList);

    }





}
