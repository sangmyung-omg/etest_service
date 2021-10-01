package com.tmax.eTest.Contents.dto.answer;

import java.util.List;

import com.tmax.eTest.Contents.dto.problem.ComponentDTO;

import lombok.Data;

@Data
public class SolutionDTO {
    private Integer probId;
    private List<String> userAnswer;
    private List<ComponentDTO> solution;
    private String material;
}