package com.tmax.eTest.Support.controller;

import com.tmax.eTest.Auth.dto.CMRespDto;
import com.tmax.eTest.Auth.dto.PrincipalDetails;
import com.tmax.eTest.Auth.repository.UserRepository;
import com.tmax.eTest.Common.model.support.Inquiry;
import com.tmax.eTest.Support.dto.CreateInquiryDto;
import com.tmax.eTest.Support.dto.DeleteInquiryDto;
import com.tmax.eTest.Support.dto.InquiryDTO;
import com.tmax.eTest.Support.dto.InquiryHistoryDTO;
import com.tmax.eTest.Support.repository.InquiryFileRepository;
import com.tmax.eTest.Support.repository.InquiryRepository;
import com.tmax.eTest.Support.service.InquiryService;
import io.swagger.models.auth.In;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@RestController
@RequestMapping("/user")
public class InquiryController {
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

    @PostMapping("/inquiry/create")
    @Transactional
    public CMRespDto<?> Inquiry(@AuthenticationPrincipal PrincipalDetails principalDetails, @ModelAttribute CreateInquiryDto createInquiryDto) {
        return new CMRespDto<>(200,"1대1 질문 생성 성공",inquiryService.createInquiry(createInquiryDto,principalDetails));
    }

    @Transactional(readOnly = true)
    @GetMapping("/inquiry/list")
    public CMRespDto<?> getInquiryList(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        List<InquiryHistoryDTO> inquiryHistoryDTOList = inquiryRepository.findAllInquiryHistoryByUserUuid(principalDetails.getUserUuid());
        return new CMRespDto<>(200,"1대1 질문 리스트 받아오기 성공",inquiryHistoryDTOList);
    }

    @Transactional(readOnly = true)
    @PostMapping("/inquiry")
    public CMRespDto<?> getInquiry(@RequestBody InquiryDTO inquiryDTO,@AuthenticationPrincipal PrincipalDetails principalDetails ) {
        System.out.println("INQUIRY_ID : "+inquiryDTO.getId());
        Optional<Inquiry> inquiryOptional = inquiryRepository.findByUserUuid(inquiryDTO.getId());
        return new CMRespDto<>(200, "1대1 질문 리스트 받아오기 성공", inquiryOptional.get());
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

    @GetMapping(value="/inquiry/display")
    public ResponseEntity<Resource> display(@Param("filename") String filename){
        String temp = path;
        Path filePath = null;
        filePath = Paths.get(temp+filename);
        HttpHeaders header = new HttpHeaders();
        System.out.println(filePath);
        try {
            header.add("Content-Type", Files.probeContentType(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Resource resource = new FileSystemResource(filePath );
        return new ResponseEntity<Resource>(resource,header, HttpStatus.OK);
    }
}
