package com.tmax.eTest.Support.dto;

import com.tmax.eTest.Common.model.user.UserMaster;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class CreateNoticeRequestDto {
    private String title;

    private String content;

    private MultipartFile image;
}
