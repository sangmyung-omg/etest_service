package com.tmax.eTest.Support.controller;

import com.tmax.eTest.Auth.dto.CMRespDto;
import com.tmax.eTest.Auth.model.PrincipalDetails;
import com.tmax.eTest.Auth.repository.UserRepository;
import com.tmax.eTest.Common.model.support.Inquiry;
import com.tmax.eTest.Support.dto.*;
import com.tmax.eTest.Support.repository.InquiryFileRepository;
import com.tmax.eTest.Support.repository.InquiryRepository;
import com.tmax.eTest.Support.service.InquiryService;
import org.apache.tika.Tika;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@RestController
public class InquiryController {
    private static final Logger logger = LoggerFactory.getLogger(InquiryController.class);

    @Value("${file.path}")
    private String path;

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

    @PostMapping(value="/user/inquiry/create")
    @Transactional
    public CMRespDto<?> Inquiry(@AuthenticationPrincipal PrincipalDetails principalDetails, @ModelAttribute CreateInquiryDto createInquiryDto) throws IOException {
        return new CMRespDto<>(200,"1대1 질문 생성 성공",inquiryService.createInquiry(createInquiryDto,principalDetails));
    }

    @Transactional(readOnly = true)
    @GetMapping("/user/inquiry/list")
    public CMRespDto<?> getInquiryList(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        List<InquiryHistoryDTO> inquiryHistoryDTOList = inquiryRepository.findAllInquiryHistoryByUserUuid(principalDetails.getUserUuid());
        return new CMRespDto<>(200,"1대1 질문 리스트 받아오기 성공",inquiryHistoryDTOList);
    }

    @Transactional(readOnly = true)
    @PostMapping("/user/inquiry")
    public CMRespDto<?> getInquiry(@RequestBody InquiryDTO inquiryDTO,@AuthenticationPrincipal PrincipalDetails principalDetails ) {
        Optional<Inquiry> inquiryOptional = inquiryRepository.findByInquiryId(inquiryDTO.getId());
        return new CMRespDto<>(200, "1대1 질문 받아오기 성공", inquiryOptional.get());
    }

    @PostMapping("/user/inquiry/delete")
    public CMRespDto<?> deleteInquiry(@RequestBody DeleteInquiryDto deleteInquiryDto, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        List<Long > userInquiryList = inquiryRepository.findAllIdByUserUuid(principalDetails.getUserUuid());
        boolean isContains = userInquiryList.contains(deleteInquiryDto.getInquiryId());
        if(isContains) {
            inquiryRepository.deleteById(deleteInquiryDto.getInquiryId());
            return new CMRespDto<>(200,"제거 성공",userInquiryList);
        }
        return new CMRespDto<>(400,"해당 질문은 유저가 만든 질문이 아닙니다","fail");
    }

    @GetMapping(value="/inquiry/attachment")
    public ResponseEntity<Resource> attachment(@Param("filename") String filename){
        String temp = path+"/inquiry/";
        Path filePath = null;
        filePath = Paths.get(temp+filename);
        HttpHeaders header = new HttpHeaders();
        Tika tika = new Tika();
        String mimeType;
        try {
                mimeType = tika.detect(filePath);
                header.add("Content-Type", mimeType);
        } catch (IOException e) {
            logger.debug("mime type Error");
        }

        Resource resource = new FileSystemResource(filePath);
        return new ResponseEntity<Resource>(resource,header, HttpStatus.OK);
    }

    @PostMapping("/user/inquiry/modify")
    @Transactional
    public CMRespDto<?> modify(@AuthenticationPrincipal PrincipalDetails principalDetails, @ModelAttribute ModifyInquiryDto modifyInquiryDto) {


        String result = inquiryService.modify(principalDetails, modifyInquiryDto);


        if (result == "유저가 생성한 질문이 아닙니다.") {
            return new CMRespDto<>(400,"실패",result);
        }
        return new CMRespDto<>(200,"성공",result);
    }

}
