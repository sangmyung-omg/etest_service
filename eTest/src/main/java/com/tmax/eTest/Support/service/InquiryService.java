package com.tmax.eTest.Support.service;

import com.tmax.eTest.Auth.dto.PrincipalDetails;
import com.tmax.eTest.Auth.repository.UserRepository;
import com.tmax.eTest.Common.model.support.Inquiry;
import com.tmax.eTest.Common.model.support.Inquiry_file;
import com.tmax.eTest.Common.model.user.UserMaster;
import com.tmax.eTest.Support.controller.InquiryController;
import com.tmax.eTest.Support.dto.CreateInquiryDto;
import com.tmax.eTest.Support.dto.ModifyInquiryDto;
import com.tmax.eTest.Support.repository.InquiryFileRepository;
import com.tmax.eTest.Support.repository.InquiryRepository;
import java.io.IOException;
import java.nio.file.Files;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.*;
import java.util.*;

@Service
public class InquiryService {
    private static final Logger logger = LoggerFactory.getLogger(InquiryService.class);

    @Autowired
    @Qualifier("SU-InquiryRepository")
    InquiryRepository inquiryRepository;
    @Autowired
    @Qualifier("SU-InquiryFileRepository")
    InquiryFileRepository inquiryFileRepository;

    @Autowired
    @Qualifier("AU-UserRepository")
    UserRepository userRepository;

    @Value("${file.path}")
    private String filePath;

    @Transactional
    public Inquiry createInquiry(CreateInquiryDto createInquiryDto, PrincipalDetails principalDetails) {
        Optional<UserMaster> userMasterOptional = userRepository.findByEmail(principalDetails.getEmail());
        UserMaster userMasterEntity= userMasterOptional.get();
        Inquiry inquiry =
                Inquiry.builder()
                        .userMaster(userMasterEntity)
                        .content(createInquiryDto.getContent())
                        .status(createInquiryDto.getStatus())
                        .title(createInquiryDto.getTitle())
                        .type(createInquiryDto.getType())
                        .build();
        inquiryRepository.save(inquiry);

        if (!(createInquiryDto.getFileList() == null)) {
            for(int i=0; i<createInquiryDto.getFileList().size(); i++){
                String fileName = UUID.randomUUID().toString() + "_" + createInquiryDto.getFileList().get(i).getOriginalFilename();
                String uploadFolder = filePath + "inquiry/";
                Path imageFilePath = Paths.get(uploadFolder + fileName);
                Inquiry_file inquiry_file = Inquiry_file.builder()
                        .name(createInquiryDto.getFileList().get(i).getOriginalFilename().replaceFirst("[.][^.]+$", "")) // 확장자 지우기
                        .size(createInquiryDto.getFileList().get(i).getSize())
                        .url(fileName)
                        .type(createInquiryDto.getFileList().get(i).getContentType())
                        .inquiry(inquiry)
                        .build();
                try {
                    Files.write(imageFilePath, createInquiryDto.getFileList().get(i).getBytes());
                } catch (IOException e) {
                    logger.debug("File write IOException");
                }
                inquiryFileRepository.save(inquiry_file);
            }
        }
        return inquiry;
    }


    @Transactional
    public String modify(@AuthenticationPrincipal PrincipalDetails principalDetails, ModifyInquiryDto modifyInquiryDto ) {

        List<Long> inquiryIdList = inquiryRepository.findAllIdByUserUuid(principalDetails.getUserUuid());
        if (!inquiryIdList.contains(modifyInquiryDto.getId())) {
            return "유저가 생성한 질문이 아닙니다.";
        }

        Optional<UserMaster> userMasterOptional = userRepository.findByUserUuid(principalDetails.getUserUuid());
        UserMaster userMaster = userMasterOptional.get();
        Optional<Inquiry> inquiryOptional = inquiryRepository.findByInquiryId(modifyInquiryDto.getId());
        Inquiry inquiry = inquiryOptional.get();
        inquiry.setUserMaster(userMaster);
        inquiry.setContent(modifyInquiryDto.getContent());
        inquiry.setTitle(modifyInquiryDto.getTitle());
        inquiry.setType(modifyInquiryDto.getType());
        String uploadFolder = filePath + "inquiry/";

        /***
         * inquiry file은 db/storage 전부 지웠다가 새로 만들고
         * inquiry는 update
         */
        // 기존의 파일 스토리지에서 delete
        for(int i =0; i < inquiry.getInquiry_file().size(); i++) {
            String url = inquiry.getInquiry_file().get(i).getUrl();
            Path filePath = Paths.get(uploadFolder + url);
            try {
                Files.delete(filePath);
            } catch (NoSuchFileException e) {
                logger.debug("해당 파일이 없습니다.");
            }
            catch (IOException e) {
                logger.debug("해당 파일이 없습니다.");
            }
        }
        // 기존의 파일 db에서 delete
        List<Long> inquiryFileNumberList = new ArrayList<>();
        for(int i =0; i < inquiry.getInquiry_file().size(); i++) {
            inquiryFileNumberList.add(inquiry.getInquiry_file().get(i).getId());
        }
        for (int i = 0; i < inquiryFileNumberList.size(); i++) {
            Long id = inquiryFileNumberList.get(i);
            inquiryFileRepository.deleteById(id);
        }



        // 새로 만들기
        for(int i=0; i<modifyInquiryDto.getFileList().size(); i++){
            String fileName = UUID.randomUUID().toString() + "_" + modifyInquiryDto.getFileList().get(i).getOriginalFilename();
            Path imageFilePath = Paths.get(uploadFolder + fileName);

            Inquiry_file inquiry_file = Inquiry_file.builder()
                    .name(modifyInquiryDto.getFileList().get(i).getOriginalFilename().replaceFirst("[.][^.]+$", ""))
                    .size(modifyInquiryDto.getFileList().get(i).getSize())
                    .url(fileName)
                    .type(modifyInquiryDto.getFileList().get(i).getContentType())
                    .inquiry(inquiry)
                    .build();
            try {
                Files.write(imageFilePath, modifyInquiryDto.getFileList().get(i).getBytes());

            } catch (Exception e) {
                e.printStackTrace();
            }
            inquiryFileRepository.save(inquiry_file);
        }
        return "True";
    }


}
