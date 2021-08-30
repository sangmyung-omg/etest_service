package com.tmax.eTest.CustomerSupport.service;


import com.tmax.eTest.Common.model.support.Inquiry;
import com.tmax.eTest.CustomerSupport.model.dto.InquiryDTO;
import com.tmax.eTest.CustomerSupport.repository.InquiryRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service("CustomerSupportInquiryService")
public class InquiryService {

    protected final Log LOGGER = LogFactory.getLog(getClass());

    @Autowired
    InquiryRepository inquiryRepository;

    public List<InquiryDTO> getInquiryList(){

        List<Inquiry> inquiryList= inquiryRepository.findAll();

        return inquiryList.stream()
                .map(i ->
                    InquiryDTO.builder()
                    .userUuid(i.getUserMaster().getUserUuid())
                    .type(i.getType())
                    .title(i.getTitle())
                    .nick_name(i.getUserMaster().getNickname())
                    .lastUpdated(i.getCreateDate())
                    .status(i.getStatus())
                    .build())
                .collect(Collectors.toList());
    }
}
