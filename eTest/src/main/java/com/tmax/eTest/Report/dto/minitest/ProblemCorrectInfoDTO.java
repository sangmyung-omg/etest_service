package com.tmax.eTest.Report.dto.minitest;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProblemCorrectInfoDTO {
    private String high;
    private String middle;
    private String low;
    
    private String allCorr;
    private String allProb;
}
