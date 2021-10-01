package com.tmax.eTest.CustomerSupport.model.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InquiryDTO {
    Long inquiryId;
    String type;
    String userNickname;
    String title;
    LocalDateTime lastUpdated;
    String status;
    LocalDateTime answerDate;
    List<InquiryFileDTO> inquiryFile;
    String content;
    String answer;
    String adminNickname;
}

