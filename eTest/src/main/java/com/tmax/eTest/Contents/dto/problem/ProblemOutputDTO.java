package com.tmax.eTest.Contents.dto.problem;

import java.util.List;

import lombok.Data;

@Data
public class ProblemOutputDTO {
    private String message;
    private String answerType;
    private List<ComponentDTO> components;
    private String difficulty;
}
