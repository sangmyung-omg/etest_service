package com.tmax.eTest.Contents.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.tmax.eTest.Contents.dto.answer.CustomizedSolutionDTO;
import com.tmax.eTest.Contents.dto.answer.Temp1SolutionDTO;
import com.tmax.eTest.LRS.dto.StatementDTO;

public interface AnswerServicesBase {
    public Integer evaluateIfCorrect(Integer probId, List<StatementDTO> lrsbody) throws Exception;
    
    public Map<String, Object> getProblemSolution(Integer problemID) throws Exception;

    public Map<String, Object> getSolutionMaterial(Integer problemID) throws Exception;

    public Map<Integer, CustomizedSolutionDTO> getMultipleSolutions(List<Integer> probIdList);

    public Map<Integer, Temp1SolutionDTO> getParsedMultipleSolutions(List<Integer> probIdLisT);
}
