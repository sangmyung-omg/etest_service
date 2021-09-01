package com.tmax.eTest.CustomerSupport.service;


import com.tmax.eTest.Common.model.support.Inquiry;
import com.tmax.eTest.CustomerSupport.model.dto.InquiryDTO;
import com.tmax.eTest.CustomerSupport.repository.InquiryRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service("CustomerSupportInquiryService")
public class InquiryService {

    protected final Log LOGGER = LogFactory.getLog(getClass());

    @Autowired
    InquiryRepository inquiryRepository;

    public List<InquiryDTO> getInquiryList(){

        List<Inquiry> inquiryList= inquiryRepository.findAll();

        //TODO adminnickname fix
        return inquiryList.stream()
                .map(i ->
                    InquiryDTO.builder()
                    .inquiryId(i.getId())
                    .userNickname(i.getUserMaster().getNickname())
                    .type(i.getType())
                    .title(i.getTitle())
                    .lastUpdated(i.getCreateDate())
                    .status(i.getStatus())
                    .answerDate(i.getAnswer_time())
                    .adminNickname(i.getAdminUuid())
                    .build())
                .collect(Collectors.toList());
    }

    public Inquiry getInquiryDetails(Long id){

        Inquiry inquiry = inquiryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No Inquiry with Given Id"));

        return inquiry;
    }
}
