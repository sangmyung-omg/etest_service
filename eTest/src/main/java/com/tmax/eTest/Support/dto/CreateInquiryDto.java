package com.tmax.eTest.Support.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tmax.eTest.Common.model.support.Inquiry_file;
import com.tmax.eTest.Common.model.user.UserMaster;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
public class CreateInquiryDto {

    private UserMaster userMaster;

    private String status;

    private String title;

    private String type;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    private String content;

    private String answer;

    private List<MultipartFile> fileList;
}
