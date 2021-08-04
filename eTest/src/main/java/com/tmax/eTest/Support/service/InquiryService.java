package com.tmax.eTest.Support.service;

import com.tmax.eTest.Common.model.support.Inquiry;
import com.tmax.eTest.Support.Dto.CreateInquiryDto;
import com.tmax.eTest.Support.repository.InquiryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InquiryService {

    @Autowired
    @Qualifier("SU-InquiryRepository")
    InquiryRepository inquiryRepository;

    @Transactional
    public Long createInquiry(CreateInquiryDto createInquiryDto) {
        Inquiry inquiry = Inquiry.builder()
                .answer(createInquiryDto.getAnswer())
                .content(createInquiryDto.getContent())
                .date(createInquiryDto.getDate())
                .status(createInquiryDto.getStatus())
                .title(createInquiryDto.getTitle())
                .type(createInquiryDto.getType())
                .URL(createInquiryDto.getURL())
                .build();
        System.out.println("inquiry.getId() 결과 :" + inquiry.getId().toString() );
        return inquiry.getId();
    }

}
