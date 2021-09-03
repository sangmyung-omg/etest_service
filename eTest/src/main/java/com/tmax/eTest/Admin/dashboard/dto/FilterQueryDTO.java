package com.tmax.eTest.Admin.dashboard.dto;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDate;

@Data
@Builder
public class FilterQueryDTO {
    private Timestamp dateFrom;
    private Timestamp dateTo;
    private String gender;
    private LocalDate ageGroupLowerBound;
    private LocalDate ageGroupUpperBound;
    private String investmentExperience;
}
