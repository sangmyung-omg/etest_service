package com.tmax.eTest.Report.dto.minitest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PartDataDTO {
    private Long score;
    private Long percentage;
    private List<List<String>> ukInfo;
}
