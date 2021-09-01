package com.tmax.eTest.CustomerSupport.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
    String adminNickname;
}
