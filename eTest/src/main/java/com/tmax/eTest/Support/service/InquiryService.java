package com.tmax.eTest.Support.service;

import com.tmax.eTest.Common.model.support.Inquiry;
import com.tmax.eTest.Common.model.support.Inquiry_file;
import com.tmax.eTest.Support.Dto.CreateInquiryDto;
import com.tmax.eTest.Support.repository.InquiryRepository;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;

@Service
public class InquiryService {

    @Autowired
    @Qualifier("SU-InquiryRepository")
    InquiryRepository inquiryRepository;

    @Transactional
    public Long createInquiry(CreateInquiryDto createInquiryDto) {
        System.out.println("inquiry service 돌입");
        System.out.println(createInquiryDto);


        List<Inquiry_file> url = createInquiryDto.getInquiry_file();
        for(Inquiry_file element : url){                                    //리스트 원소 돌면서 각 Url에 UUID를 붙여줘서 고유하게 만들어준다.
            String element_url = element.getUrl();                          // 첨부파일 -> 질문 -> 유저 조회하는게 맞는가? 첨부파일 -> 유저로 조회하는게 맞는가?
            element.setUrl(UUID.randomUUID().toString() + element_url);
            System.out.println(element.getUrl());

        }

        createInquiryDto.setInquiry_file(url);



        Inquiry inquiry = Inquiry.builder()
                .answer(createInquiryDto.getAnswer())
                .content(createInquiryDto.getContent())
                .date(createInquiryDto.getDate())
                .status(createInquiryDto.getStatus())
                .title(createInquiryDto.getTitle())
                .type(createInquiryDto.getType())
                .inquiry_file(createInquiryDto.getInquiry_file())
                .build();
        System.out.println("inquiry.getId() 결과 :" + inquiry.getId().toString() );
        return inquiry.getId();
    }

}
