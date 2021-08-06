package com.tmax.eTest.Support.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@NoArgsConstructor
public class CreateNoticeDto {
    private String title;

    private LocalDate date;

    private String content;
}
