package com.tmax.eTest.Contents.service;

import java.util.List;

import com.tmax.eTest.Contents.dto.problem.ProblemDTO;
import com.tmax.eTest.Contents.dto.problem.Temp0ProblemOutputDTO;
import com.tmax.eTest.Contents.dto.problem.Temp1ProblemOutputDTO;

public interface ProblemServicesBase {
    public Temp1ProblemOutputDTO getProblemInfo(Integer probId) throws Exception;
    public Temp0ProblemOutputDTO getParsedProblemInfo(Integer probId) throws Exception;
    public ProblemDTO getProblem(Integer problemID) throws Exception;
    public List<Integer> getDiagnosisProblem(String setNum) throws Exception;
    public String insertErrorReport(long problemID, String id, String reportType, String reportText) throws Exception;
}
