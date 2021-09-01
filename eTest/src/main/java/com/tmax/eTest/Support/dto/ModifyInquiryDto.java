package com.tmax.eTest.Support.dto;

import com.tmax.eTest.Common.model.support.Inquiry_file;
import com.tmax.eTest.Common.model.user.UserMaster;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ModifyInquiryDto {
    private Long id;

    private String status;

    private String title;

    private String type;

    private String content;

    private String answer;

    private List<MultipartFile> fileList;

    private LocalDateTime createDate;

}
