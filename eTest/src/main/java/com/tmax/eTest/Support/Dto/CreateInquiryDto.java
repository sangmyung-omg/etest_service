package com.tmax.eTest.Support.Dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class CreateInquiryDto {

    private String status;

    private String title;

    private String type;

    private LocalDate date;

    private String content;

    private String URL;

    private String answer;
}
