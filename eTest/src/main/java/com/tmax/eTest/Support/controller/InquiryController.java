package com.tmax.eTest.Support.controller;

import com.tmax.eTest.Auth.dto.CMRespDto;
import com.tmax.eTest.Support.Dto.CreateInquiryDto;
import com.tmax.eTest.Support.service.InquiryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InquiryController {

    @Autowired
    InquiryService inquiryService;


    @PostMapping("/Inquiry/create")
    public CMRespDto<?> Inquiry(CreateInquiryDto createInquiryDto) {
            Long inquiryId = inquiryService.createInquiry(createInquiryDto);
        return new CMRespDto<>(200,"1대1 질문 생성 성공",inquiryId);
    }

}
