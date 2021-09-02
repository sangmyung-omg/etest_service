package com.tmax.eTest.Test.dto;

import java.util.List;

import lombok.Data;

@Data
public class DiagnosisOutputDTO {
    private final String resultMessage;
    private final String diagProbSetId;
    private final List<Integer> tendencyProblems;
    private final List<List<Integer>> knowledgeProblems;
    private final String NRUuid;
}