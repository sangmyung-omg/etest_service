package com.tmax.eTest.Contents.dto.answer;

import java.util.List;

import lombok.Data;

@Data
public class CustomizedSolutionDTO {
    private Integer probId;
    private List<String> userAnswer;
    private String solution;
    private String material;
}
