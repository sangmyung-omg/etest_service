package com.tmax.eTest.Admin.dashboard.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FilterDTO {
    private LocalDateTime dateFrom;
    private LocalDateTime dateTo;
    private String gender;
    private Integer ageGroup;
    private String investmentExperience;
}


