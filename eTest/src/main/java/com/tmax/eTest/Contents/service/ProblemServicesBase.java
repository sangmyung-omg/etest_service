package com.tmax.eTest.Contents.service;

import java.util.List;

import com.tmax.eTest.Contents.dto.problem.ProblemDTO;
import com.tmax.eTest.Contents.dto.problem.ProblemOutputDTO;

public interface ProblemServicesBase {
    public ProblemOutputDTO getProblemInfo(Integer probId) throws Exception;
    public ProblemDTO getProblem(Integer problemID) throws Exception;
    public List<Integer> getDiagnosisProblem(String setNum) throws Exception;
    public String insertErrorReport(long problemID, String id, String reportType, String reportText) throws Exception;
}
