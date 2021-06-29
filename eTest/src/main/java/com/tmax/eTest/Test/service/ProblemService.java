package com.tmax.eTest.Test.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tmax.eTest.Contents.model.DiagnosisProblem;
import com.tmax.eTest.Contents.model.TestProblem;
import com.tmax.eTest.Contents.repository.DiagnosisProblemRepository;
import com.tmax.eTest.Contents.repository.TestProblemRepository;


@Service
public class ProblemService {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
	
	@Autowired
	DiagnosisProblemRepository diagnosisRepo;
	TestProblemRepository minitestRepo;
	
	
	public Map<String, Object> getDiagnosisProblems(String type){
		Map<String, Object> map = new HashMap<String,Object>();
		String queryType = "";
		if (type.equalsIgnoreCase("tendency")) {
			queryType = "성향";
			List<Integer> tendencyProblems = new ArrayList<Integer>();
			
			// 일단 성향 문제  fix된 세트만 제공한다는 가정. (그냥 있는 문제 다 표출)
			logger.info("Getting all diagnosis tendency problem info......");
			tendencyProblems = diagnosisRepo.findTendencyProblems(queryType);
			Collections.shuffle(tendencyProblems);
			map.put("tendencyProblems", tendencyProblems);				// [1,2,3,4,5,6,7,8,9]의 형태
			
		} else if (type.equalsIgnoreCase("knowledge")) {
			queryType = "지식";
			List<List<Integer>> knowledgeProblems = new ArrayList<List<Integer>>();
			
			// 일단 진단 지식 문제 다 불러와서 판단
			logger.info("Getting all diagnosis knowledge problem info......");
			List<DiagnosisProblem> total_list = diagnosisRepo.findAll();
			
			// 주제별, 난이도별 문제 수집
			Map<String, Map<String, List<Integer>>> subject_diff_map = new HashMap<String, Map<String, List<Integer>>>();
			for (DiagnosisProblem dto : total_list) {
				if (!subject_diff_map.containsKey(dto.getSubject())) {
					subject_diff_map.put(dto.getSubject(), new HashMap<String, List<Integer>>());
				}
				
				if (dto.getProblem().getDifficulty().equalsIgnoreCase("상")) {
					if (subject_diff_map.get(dto.getSubject()).containsKey("high")) {
						subject_diff_map.get(dto.getSubject()).get("high").add(dto.getProbID());
					} else {
						subject_diff_map.get(dto.getSubject()).put("high", Arrays.asList(dto.getProbID()));
					}
				} else if (dto.getProblem().getDifficulty().equalsIgnoreCase("중")) {
					if (subject_diff_map.get(dto.getSubject()).containsKey("mid")) {
						subject_diff_map.get(dto.getSubject()).get("mid").add(dto.getProbID());
					} else {
						subject_diff_map.get(dto.getSubject()).put("mid", Arrays.asList(dto.getProbID()));
					}
				} else if (dto.getProblem().getDifficulty().equalsIgnoreCase("하")) {
					if (subject_diff_map.get(dto.getSubject()).containsKey("low")) {
						subject_diff_map.get(dto.getSubject()).get("low").add(dto.getProbID());
					} else {
						subject_diff_map.get(dto.getSubject()).put("low", Arrays.asList(dto.getProbID()));
					}
				}
			}
			
			// 각 주제별, 난이도별로 적어도 한 문제씩은 있다고 가정. (없으면 adaptive 할 수가 없음)
			for (String key : subject_diff_map.keySet()) {
				Collections.shuffle(subject_diff_map.get(key).get("low"));
				Collections.shuffle(subject_diff_map.get(key).get("mid"));
				Collections.shuffle(subject_diff_map.get(key).get("high"));
				
				knowledgeProblems.add(Arrays.asList(subject_diff_map.get(key).get("low").get(0),
													subject_diff_map.get(key).get("mid").get(0),
													subject_diff_map.get(key).get("high").get(0)));
			}
			
			Collections.shuffle(knowledgeProblems);
			map.put("knowledgeProblems", knowledgeProblems);		// [[1,2,3],[4,5,6],[7,8,9],...] 의 형태
		}
		
		
		return map;
	}
	
	public Map<String, Object> getMinitestProblems(Integer set_num){
		Map<String, Object> map = new HashMap<String, Object>();
		if (set_num == null || set_num == 0) {
			// 미니테스트 세트의 개수가 자주 변한다면 (지속적으로 추가된다면)
			Integer max_setNum = minitestRepo.findMaximumSetNum();
			if (max_setNum == 0 || max_setNum == null) {
				map.put("error", "No minitest problems in DB");
				return map;
			}
			List<Integer> setNumList = new ArrayList<Integer>();
			for (int i=0; i<max_setNum; i++) {
				setNumList.add(i+1);
			}
			Collections.shuffle(setNumList);
			set_num = setNumList.get(0);
		}
		
		List<Integer> minitestProblems = new ArrayList<Integer>();
		for (TestProblem problem : minitestRepo.findSetProblems(set_num, 0)) {
			minitestProblems.add(problem.getProbID());
		}
		
		if (minitestProblems == null || minitestProblems.size() == 0) {
			String err_msg = "No minitest problems for set_num : " + Integer.toString(set_num);
			
			if (map.containsKey("error")) map.replace("error", map.get("error") + "\n" + err_msg);				
			else map.put("error", err_msg);
		}
		
		map.put("minitestProblems", minitestProblems);
		
		return map;
	}
}
