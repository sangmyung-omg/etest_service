package com.tmax.eTest.CustomerSupport.service;


import com.tmax.eTest.Common.model.support.Inquiry;
import com.tmax.eTest.CustomerSupport.model.dto.InquiryDTO;
import com.tmax.eTest.CustomerSupport.model.dto.InquiryFileDTO;
import com.tmax.eTest.CustomerSupport.repository.InquiryRepository;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    public InquiryDTO getInquiryDetails(Long id){

        Inquiry inquiry = inquiryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No Inquiry with Given Id"));

        return InquiryDTO.builder()
                .inquiryId(inquiry.getId())
                .type(inquiry.getType())
                .userNickname(inquiry.getUserMaster().getNickname())
                .title(inquiry.getTitle())
                .lastUpdated(inquiry.getCreateDate())
                .status(inquiry.getStatus())
                .content(inquiry.getContent())
                .answer(inquiry.getAnswer())
                .answerDate(inquiry.getAnswer_time())
                .inquiryFile(inquiry.getInquiry_file().stream()
                        .map(i -> InquiryFileDTO.builder()
                                .id(i.getId())
                                .url(i.getUrl())
                                .type(i.getType())
                                .name(i.getName())
                                .size(i.getSize())
                                .build()).collect(Collectors.toList()))
                .adminNickname(inquiry.getAdminUuid())
                .build();
    }

    public InquiryDTO answerInquiry(Long id,String admin_uuid, String admin_nickname, String answer){
        if (StringUtils.isEmpty(answer)){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Answer cannot be null or emtpy");
        }

        Inquiry inquiry = inquiryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No Inquiry with Given Id"));

        inquiry.setAdminUuid(admin_uuid);
        inquiry.setAnswer(answer);
        inquiry.setStatus("답변완료");
        inquiry.setAnswer_time(LocalDateTime.now());

        inquiryRepository.save(inquiry);

        return InquiryDTO.builder()
                .inquiryId(inquiry.getId())
                .type(inquiry.getType())
                .userNickname(inquiry.getUserMaster().getNickname())
                .title(inquiry.getTitle())
                .lastUpdated(inquiry.getAnswer_time())
                .status(inquiry.getStatus())
                .content(inquiry.getContent())
                .answer(inquiry.getAnswer())
                .answerDate(inquiry.getAnswer_time())
                .inquiryFile(inquiry.getInquiry_file().stream()
                        .map(i -> InquiryFileDTO.builder()
                                .id(i.getId())
                                .url(i.getUrl())
                                .type(i.getType())
                                .name(i.getName())
                                .size(i.getSize())
                                .build()).collect(Collectors.toList()))
                .adminNickname(admin_nickname)
                .build();
    }

    public void deleteInquiry(Long id) {
        Inquiry inquiry = inquiryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No Inquiry with Given Id"));
        inquiryRepository.delete(inquiry);
    }

}
