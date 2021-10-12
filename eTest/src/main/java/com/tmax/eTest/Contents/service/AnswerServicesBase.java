package com.tmax.eTest.Contents.service;

import java.util.List;
import java.util.Map;

import com.tmax.eTest.Contents.dto.answer.CustomizedSolutionDTO;
import com.tmax.eTest.Contents.dto.answer.SolutionDTO;
import com.tmax.eTest.LRS.dto.StatementDTO;

public interface AnswerServicesBase {
    public Integer evaluateIfCorrect(Integer probId, List<StatementDTO> lrsbody);
    
    public Map<String, Object> getProblemSolution(Integer problemID);

    public Map<String, Object> getSolutionMaterial(Integer problemID);

    public Map<Integer, CustomizedSolutionDTO> getMultipleSolutions(List<Integer> probIdList);

    public Map<Integer, SolutionDTO> getParsedMultipleSolutions(List<Integer> probIdLisT);
}
