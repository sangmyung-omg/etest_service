package com.tmax.eTest.Contents.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.tmax.eTest.Contents.dto.CustomizedSolutionDTO;
import com.tmax.eTest.LRS.dto.StatementDTO;

public interface AnswerServicesBase {
    public Integer evaluateIfCorrect(Integer probId, ArrayList<StatementDTO> lrsbody) throws Exception;
    
    public Map<String, Object> getProblemSolution(Integer problemID) throws Exception;

    public Map<String, Object> getSolutionMaterial(Integer problemID) throws Exception;

    public Map<Integer, CustomizedSolutionDTO> getMultipleSolutions(List<Integer> probIdList);
}
