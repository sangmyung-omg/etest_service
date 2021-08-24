package com.tmax.eTest.Test.service;

import java.util.Map;

public interface ProblemServiceBase {
    public Map<String, Object> getDiagnosisTendencyProblems();
    public Map<String, Object> getDiagnosisKnowledgeProblems();
    public Map<String, Object> getMinitestProblems(String userId);
    public Map<String, Object> getMinitestProblemsV0(Integer userId);
    
}
