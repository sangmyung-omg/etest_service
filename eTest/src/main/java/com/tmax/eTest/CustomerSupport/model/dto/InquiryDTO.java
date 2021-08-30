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

    String userUuid;
    String type;
    String title;
    String nick_name;
    LocalDateTime lastUpdated;
    String status;
    LocalDateTime answerDate;
    String adminUuid;

    public void setDate(){
        this.lastUpdated = LocalDateTime.now();
    }

}
