package com.tmax.eTest.Contents.dto.problem;

import java.util.List;

import lombok.Data;

@Data
public class Temp0ProblemOutputDTO {
    private List<String> componentTypeList;
    private List<String> questionText;
    private List<String> exampleBoxText;
    private List<String> multipleChoiceText;
    private List<String> questionImage;
    private List<String> multipleChoiceImage;
    private List<String> exampleBoxImage;
    private String preface;
    private Integer shortAnswerText;
    private String answerType;
    private String difficulty;
    private String message;
}
