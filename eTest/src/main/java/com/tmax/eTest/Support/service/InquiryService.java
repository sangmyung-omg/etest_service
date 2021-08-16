package com.tmax.eTest.Support.service;

import com.tmax.eTest.Auth.dto.PrincipalDetails;
import com.tmax.eTest.Auth.repository.UserRepository;
import com.tmax.eTest.Common.model.support.Inquiry;
import com.tmax.eTest.Common.model.support.Inquiry_file;
import com.tmax.eTest.Common.model.user.UserMaster;
import com.tmax.eTest.Support.dto.CreateInquiryDto;
import com.tmax.eTest.Support.repository.InquiryFileRepository;
import com.tmax.eTest.Support.repository.InquiryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class InquiryService {

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
    private String uploadFolder;

    @Transactional
    public Inquiry createInquiry(CreateInquiryDto createInquiryDto, PrincipalDetails principalDetails) {
        List<MultipartFile> fileList = createInquiryDto.getFileList();
        Optional<UserMaster> userMaster_temp = userRepository.findByEmail(principalDetails.getEmail());
        UserMaster userMasterEntity= userMaster_temp.get();
        Inquiry inquiry =
                Inquiry.builder()
                        .userMaster(userMasterEntity)
                        .content(createInquiryDto.getContent())
                        .status(createInquiryDto.getStatus())
                        .date(createInquiryDto.getDate())
                        .title(createInquiryDto.getTitle())
                        .type(createInquiryDto.getType())
                        .build();
        inquiryRepository.save(inquiry);

        for(int i=0; i<createInquiryDto.getFileList().size(); i++){
            String fileName = UUID.randomUUID().toString() + "_" + createInquiryDto.getFileList().get(i).getOriginalFilename();
            Path imageFilePath = Paths.get(uploadFolder + fileName);
            Inquiry_file inquiry_file = Inquiry_file.builder()
                    .url(fileName)
                    .type(createInquiryDto.getFileList().get(i).getContentType())
                    .inquiry(inquiry)
                    .build();
            try {

                Files.write(imageFilePath, createInquiryDto.getFileList().get(i).getBytes());
            } catch (Exception e) {
                e.printStackTrace();
            }
            inquiryFileRepository.save(inquiry_file);
        }


        return inquiry;
    }

}
