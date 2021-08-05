package com.tmax.eTest.Support.Dto;

import com.tmax.eTest.Common.model.support.Inquiry_file;
import com.tmax.eTest.Common.model.user.UserMaster;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
public class CreateInquiryDto {

    private UserMaster userMaster;

    private String status;

    private String title;

    private String type;

    private LocalDate date;

    private String content;

    private String answer;

    private List<Inquiry_file> inquiry_file;
}
