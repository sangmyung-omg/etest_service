package com.tmax.eTest.Report.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReportShareCreateDTO {
    private String type = "minitest";
    private String probSetId;
    private String expire = "100000000";
}
