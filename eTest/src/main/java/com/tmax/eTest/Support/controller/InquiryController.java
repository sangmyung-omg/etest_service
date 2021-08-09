package com.tmax.eTest.Support.controller;

import com.tmax.eTest.Auth.dto.CMRespDto;
import com.tmax.eTest.Auth.dto.PrincipalDetails;
import com.tmax.eTest.Auth.repository.UserRepository;
import com.tmax.eTest.Common.model.support.Inquiry;
import com.tmax.eTest.Common.model.support.Inquiry_file;
import com.tmax.eTest.Common.model.user.UserMaster;
import com.tmax.eTest.Support.dto.CreateInquiryDto;
import com.tmax.eTest.Support.dto.DeleteInquiryDto;
import com.tmax.eTest.Support.repository.InquiryFileRepository;
import com.tmax.eTest.Support.repository.InquiryRepository;
import com.tmax.eTest.Support.service.InquiryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@RestController
@RequestMapping("/user")
public class InquiryController {

    @Autowired
    @Qualifier("SU-InquiryRepository")
    InquiryRepository inquiryRepository;

    @Autowired
    @Qualifier("AU-UserRepository")
    UserRepository userRepository;

    @Autowired
    @Qualifier("SU-InquiryFileRepository")
    InquiryFileRepository inquiryFileRepository;

    @Autowired
    InquiryService inquiryService;

    @PostMapping("/inquiry/create")
    @Transactional
    public CMRespDto<?> Inquiry(@AuthenticationPrincipal PrincipalDetails principalDetails, @ModelAttribute CreateInquiryDto createInquiryDto) {
        return new CMRespDto<>(200,"1대1 질문 생성 성공",inquiryService.createInquiry(createInquiryDto,principalDetails));
    }

    @Transactional(readOnly = true)
    @PostMapping("/inquiry/list")
    public CMRespDto<?> getInquiryList(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        List<Inquiry> inquiryList = inquiryRepository.findAllByUserUuid(principalDetails.getUserUuid());
        return new CMRespDto<>(200,"1대1 질문 리스트 받아오기 성공",inquiryList);
    }

    @PostMapping("/inquiry/delete")
    public CMRespDto<?> deleteInquiry(@RequestBody DeleteInquiryDto deleteInquiryDto, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        List<Long > userInquiryList = inquiryRepository.findAllIdByUserUuid(principalDetails.getUserUuid());
        boolean isContains = userInquiryList.contains(deleteInquiryDto.getInquiryId());
        if(isContains) {
            inquiryRepository.deleteById(deleteInquiryDto.getInquiryId());
            return new CMRespDto<>(200,"제거 성공",userInquiryList);
        }
        return new CMRespDto<>(400,"해당 질문은 유저가 만든 질문이 아닙니다","fail");
    }
}

