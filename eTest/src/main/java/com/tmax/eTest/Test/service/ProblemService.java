package com.tmax.eTest.Test.service;

import java.util.ArrayList;
import java.util.Random;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.*;

import java.text.ParseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tmax.eTest.Common.model.problem.DiagnosisCurriculum;
import com.tmax.eTest.Common.model.problem.DiagnosisProblem;
import com.tmax.eTest.Common.model.problem.Problem;
import com.tmax.eTest.Common.model.problem.TestProblem;
import com.tmax.eTest.Contents.repository.DiagnosisCurriculumRepository;
import com.tmax.eTest.Contents.repository.DiagnosisProblemRepository;
import com.tmax.eTest.Contents.repository.ProblemRepository;
import com.tmax.eTest.Contents.repository.TestProblemRepository;
import com.tmax.eTest.Report.dto.lrs.GetStatementInfoDTO;
import com.tmax.eTest.Report.dto.lrs.StatementDTO;
import com.tmax.eTest.Report.util.LRSAPIManager;

@Service
public class ProblemService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

	@Autowired
	DiagnosisProblemRepository diagnosisRepo;

	@Autowired
	DiagnosisCurriculumRepository diagnosisCurriculumRepo;

	@Autowired
	TestProblemRepository minitestRepo;
	
	@Autowired
	ProblemRepository problemRepo;

	@Autowired
	LRSAPIManager lrsAPIManager;
	
	public Map<String, Object> getDiagnosisTendencyProblems() {
		Map<String, Object> map = new HashMap<String, Object>();
		String type = "성향";

		logger.info("Getting diagnosis curriculums info for " + type + "......");
		// 진단 커리큘럼 테이블에서 type에 해당되는 커리큘럼 아이디를 모두 가져옴
		List<DiagnosisCurriculum> selectedCurriculum = diagnosisCurriculumRepo.findByChapterOrderByCurriculumIdAsc(type);
		
		List<Integer> selectedCurriculumId = selectedCurriculum.stream().map(dc -> dc.getCurriculumId())
				.collect(toList());
		logger.info(String.format("Retrieved all curriculum ids with type %s", type));

		// 성향문제의 경우 : 가져온 커리큘럼 아이디에 해당되는 모든 진단 문제를 하나의 리스트에 합침
		List<Integer> problemLists = new ArrayList<Integer>();
		String choice = Arrays.asList("A", "B", "C").get(new Random().nextInt(3));
		for (Integer i : selectedCurriculumId) {
//			logger.info("Getting diagnosis problems for the selected curriculumId : " + i);
			List<DiagnosisProblem> selectedProblems = diagnosisRepo.findByCurriculumIdOrderByOrderNumAsc(i);
			List<Integer> selectedProbIds = new ArrayList<Integer>();
			// curriculum id 7, 8은 선택된 set_type (A, B, C) 에 해당하는 문제만 선택
			if (i < 7) {
				selectedProbIds = selectedProblems.stream().map(sp -> sp.getProbId()).collect(toList());
			} else {
				selectedProbIds = selectedProblems.stream().filter(sp -> sp.getCurriculum().getSetType().equals(choice))
						.map(sp -> sp.getProbId()).collect(toList());
			}
			problemLists.addAll(selectedProbIds);
		}
		map.put("tendencyProblems", problemLists); // [1,2,3,4,5,6,7,8,9]의 형태

		return map;
	}

	public Map<String, Object> getDiagnosisKnowledgeProblems() {
		Map<String, Object> map = new HashMap<String, Object>();
		String type = "지식";
		
		logger.info("Getting knowledge diagnosis problems info......");
		List<Problem> selectedProblems2 = problemRepo.findByCategoryOrderByDiagnosisInfoCurriculumIdAscDiagnosisInfoOrderNumAscDifficultyAsc(type);
		
		// 지식문제의 경우 : 특정 진단 주제들에 해당되는 진단문제 리스트들을 리스트 안에 넣음
		List<List<Integer>> problemList = new ArrayList<List<Integer>>();
		List<String> subjectList = new ArrayList<String>();					// 주제마다 List 하나
		List<String> difficultyList = new ArrayList<String>();				// 난이도 순서 고려
		List<Integer> probIdList = new ArrayList<Integer>();				// 문제 세트 ID List 생성
		for (Problem p : selectedProblems2) {
			if (!subjectList.contains(p.getDiagnosisInfo().getCurriculum().getSubject())) { //jinhyung edit 210804
				subjectList.add(p.getDiagnosisInfo().getCurriculum().getSubject());
				
				// considering difficulty
				List<Integer> tempList = new ArrayList<Integer>();
				if (difficultyList.contains("상")) tempList.add(probIdList.get(difficultyList.indexOf("상")));
				if (difficultyList.contains("중")) tempList.add(probIdList.get(difficultyList.indexOf("중")));
				if (difficultyList.contains("하")) tempList.add(probIdList.get(difficultyList.indexOf("하")));
				problemList.add(tempList);
				
				probIdList.clear();
				difficultyList.clear();
			}
			
			difficultyList.add(p.getDifficulty());
			probIdList.add(p.getProbID());

//			logger.info(Integer.toString(p.getProbID()) + ", " + p.getCategory() + ", " + p.getDifficulty() + ", " + p.getDiagnosisInfo().getSubject() + ", " + p.getDiagnosisInfo().getCurriculumId() + ", " + Integer.toString(p.getDiagnosisInfo().getOrderNum()));
		}
		problemList.remove(0);
		/*
		 * 여러 조건으로 한 번에 query 가능.
		 * findByCurriculumIdIn(List<String> selectedCurriculumId);
		 */
		
		map.put("knowledgeProblems", problemList);

		return map;
	}

	public Map<String, Object> getMinitestProblems(String userId) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Integer> minitestProblems = new ArrayList<Integer>();

		/*
		*	LRS에서 문제 풀이 이력 참고해 중복 방지.
		*/
		List<StatementDTO> statementQuery = new ArrayList<StatementDTO>();
		GetStatementInfoDTO statementInput = new GetStatementInfoDTO();
		statementInput.pushUserId(userId);
		statementInput.pushSourceType("mini_test_question");
		statementInput.pushActionType("submit");

		logger.info("Getting LRS log data......");
		try {
			statementQuery = lrsAPIManager.getStatementList(statementInput);
			logger.info("length of the statement query result : " + Integer.toString(statementQuery.size()));
		} catch (ParseException e) {
			logger.info(e.getMessage());
			map.put("error", e.getMessage());
			return map;
		}

		if (statementQuery.size() == 0) {
			logger.info("Result of LRS statement query for ID " + userId +" is empty");
			map.put("error", "Result of LRS statement query for ID " + userId +" is empty");
			return map;
		}

		// 푼 문제 리스트
		List<Integer> solvedProbList = new ArrayList<Integer>();

		for (StatementDTO query : statementQuery) {
			solvedProbList.add(Integer.parseInt(query.getSourceId()));
		}

		/*
		*	중복 배제하여 미니진단 문제 조회 - 전체 조회 후 개수에 맞게 분배
		*/
		logger.info("Getting minitest problem infos......");
		List<TestProblem> minitestQueryResult = minitestRepo.findAllByProbIDNotIn(solvedProbList);
		logger.info(Integer.toString(minitestQueryResult.get(0).getProbID()));
		logger.info(Integer.toString(minitestQueryResult.size()));

		Map<Integer, List<Integer>> partProbIdMap = new HashMap<Integer, List<Integer>>();		// 각 파트 별로 문제 ID 수집
		Map<Integer, List<Integer>> partNumOrderMap = new HashMap<Integer, List<Integer>>();	// 각 파트의 [순서, 출제 문제 개수] 정보 저장.
		for (TestProblem problem : minitestQueryResult) {
			// logger.info(Integer.toString(problem.getProbID()) + ", " + problem.getProblem().getCategory() + ", " + problem.getProblem().getDifficulty() + ", " + problem.getStatus());
			if (problem.getPart() == null) {
				map.put("error", "No part mapping info for probId : " + Integer.toString(problem.getProbID()) + ", DB data issue.\nPlease check if data is filled in 'PART' table");
				return map;
			}
			// 파트 별 문제 수집
			if (!partProbIdMap.containsKey(problem.getPart().getPartID())) {
				partProbIdMap.put(problem.getPart().getPartID(), Arrays.asList(problem.getProbID()));
			} else {
				partProbIdMap.get(problem.getPart().getPartID()).add(problem.getProbID());
			}

			// 파트 별 [파트 순서, 출제 문제 개수] 수집
			if (!partNumOrderMap.containsKey(problem.getPart().getPartID())){
				partNumOrderMap.put(problem.getPart().getPartID(), Arrays.asList(problem.getPart().getOrderNum(), problem.getPart().getProblemCount()));
			}
		}

		// 파트 순서로 정렬
		List<Map.Entry<Integer, List<Integer>>> orderList = new ArrayList<Map.Entry<Integer, List<Integer>>>(partNumOrderMap.entrySet());
		Collections.sort(orderList, new Comparator<HashMap.Entry<Integer, List<Integer>>>(){
			@Override
			public int compare(HashMap.Entry<Integer, List<Integer>> o1, HashMap.Entry<Integer, List<Integer>> o2) {
				return o1.getValue().get(0).compareTo(o2.getValue().get(0));
			}
		});

		// 순서대로 각 파트 별 문제 리스트에서 설정된 개수 만큼 랜덤으로 선택하여 수집
		for (Map.Entry<Integer, List<Integer>> part : orderList) {
			List<Integer> probIdList = partProbIdMap.get(part.getKey());
			Collections.sort(probIdList);
			for (int i=0 ; i<part.getValue().get(1) ; i++) {
				minitestProblems.add(probIdList.get(i));
			}
		}
		// if (set_num == null || set_num == 0) {
		// 	// 미니테스트 세트의 개수가 자주 변한다면 (지속적으로 추가된다면)
		// 	Integer max_setNum = minitestRepo.findMaximumSetNum();
		// 	if (max_setNum == 0 || max_setNum == null) {
		// 		map.put("error", "No minitest problems in DB");
		// 		return map;
		// 	}
		// 	List<Integer> setNumList = new ArrayList<Integer>();
		// 	for (int i = 0; i < max_setNum; i++) {
		// 		setNumList.add(i + 1);
		// 	}
		// 	Collections.shuffle(setNumList);
		// 	set_num = setNumList.get(0);
		// }

		// List<Integer> minitestProblems = new ArrayList<Integer>();
		// for (TestProblem problem : minitestRepo.findSetProblems(set_num, 0)) {
		// 	minitestProblems.add(problem.getProbID());
		// }

		// if (minitestProblems == null || minitestProblems.size() == 0) {
		// 	String err_msg = "No minitest problems for set_num : " + Integer.toString(set_num);

		// 	if (map.containsKey("error"))
		// 		map.replace("error", map.get("error") + "\n" + err_msg);
		// 	else
		// 		map.put("error", err_msg);
		// }

		map.put("minitestProblems", minitestProblems);

		return map;
	}
}
