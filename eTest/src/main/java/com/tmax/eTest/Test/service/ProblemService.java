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
	
	class MinitestInfo {
		Map<Integer, List<Integer>> partProbIdMap;
		Map<Integer, List<Integer>> partNumOrderMap;
		void set(Map<Integer, List<Integer>> map1, Map<Integer, List<Integer>> map2) {
			partProbIdMap = map1;
			partNumOrderMap = map2;
		}

		Map<Integer, List<Integer>> getProbMap() {
			return partProbIdMap;
		}

		Map<Integer, List<Integer>> getNumOrderMap() {
			return partNumOrderMap;
		}
	}

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
		final Integer TOTAL_PART_NUM = 5;
		final Integer TOTAL_PROB_NUM = 20;
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

		/*
			8/12 (목) : LRS 데이터에서 가장 최근 시험 본 미니진단에서 20문제 모두 풀었는지 확인해서, 다 못 풀었으면 해당 세트 ID에 해당하는 문제 모두 불러오기
			각 푼 문제가 어떤 파트의 문제인지 & 어떤 답을 답했는지 DB에서 조회해서 미리 문제 수집하는 map이나 리턴값에 넣어놓기
		*/

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

		MinitestInfo minitestInfo = new MinitestInfo();
		minitestInfo = parseMinitestQueryResult(partProbIdMap, partNumOrderMap, minitestQueryResult);

		partProbIdMap = minitestInfo.getProbMap();
		partNumOrderMap = minitestInfo.getNumOrderMap();

		// 함수화로 대체된 코드 (테스트 후 삭제 예정)
		// for (TestProblem problem : minitestQueryResult) {
		// 	// logger.info(Integer.toString(problem.getProbID()) + ", " + problem.getProblem().getCategory() + ", " + problem.getProblem().getDifficulty() + ", " + problem.getStatus());
		// 	if (problem.getPart() == null) {
		// 		logger.info("No part mapping info for probId : " + Integer.toString(problem.getProbID()) + ", DB data issue.\nPlease check if data is filled in 'PART' table");
		// 		continue;
		// 	}
		// 	// 파트 별 문제 수집
		// 	if (!partProbIdMap.containsKey(problem.getPart().getPartID())) {
		// 		partProbIdMap.put(problem.getPart().getPartID(), Arrays.asList(problem.getProbID()));
		// 	} else {
		// 		partProbIdMap.get(problem.getPart().getPartID()).add(problem.getProbID());
		// 	}

		// 	// 파트 별 [파트 순서, 출제 문제 개수] 수집
		// 	if (!partNumOrderMap.containsKey(problem.getPart().getPartID())){
		// 		partNumOrderMap.put(problem.getPart().getPartID(), Arrays.asList(problem.getPart().getOrderNum(), problem.getPart().getProblemCount()));
		// 	}
		// }

		/*
		* 8/6 추가 개발 필요 사항. : 사용자가 많이 풀어서 DB에 더 이상 중복 배제 남은 문제 없을 경우.
		// 모든 파트(5개)가 있는지 확인

		// 파트의 문제 개수가 설정된 [출제 문제 개수] 이상 존재하는지 확인

		* (아예 없는 파트 + 문제 개수 부족한 파트) 에 해당하는 전체 문제 (중복 허용) 다시 DB 조회해와서 partProbIdMap과 partNumOrderMap에 추가해줘야 됨.
		* 쿼리 할 때 : WHERE PART_ID NOT IN (현재 조건을 다 충족하는 파트들 ID)
		*/
		List<Integer> partIdAlready = new ArrayList<Integer>();
		List<Integer> probIdAlready = new ArrayList<Integer>();
		for (Integer partId : partProbIdMap.keySet()) {
			if (partProbIdMap.get(partId).size() < partNumOrderMap.get(partId).get(1)){
				for (Integer probId : partProbIdMap.get(partId)) {
					probIdAlready.add(probId);
				}
			} else if (!partIdAlready.contains(partId)) partIdAlready.add(partId);
		}

		// 이미 푼 문제 제외하고 조회했는데 필요한 개수 충족 X. --> 현재 가져와놓은 문제 외에는 모두 풀어봤단 의미이므로, 가져온 문제 제외하고 그냥 전체에서 랜덤 출제.
		// 전체 한 번 이상 다 푼 사람은 추후에.
		if (partIdAlready.size() < TOTAL_PART_NUM){
			logger.info("Only " + Integer.toString(partIdAlready.size()) + " parts are satisfied : " + partIdAlready.toString() + ", " + probIdAlready.toString());
			logger.info("Getting supplementary minitest problem infos......");
			Map<Integer, List<Integer>> additionalProbMap = new HashMap<Integer, List<Integer>>();
			
			// 쿼리 결과를 각 파트 별로 문제 분리. 새로 조회된 파트는 파트 순서 & 문제 수 정보도 수집.
			minitestInfo = parseMinitestQueryResult(additionalProbMap, partNumOrderMap, minitestRepo.findAllByPartPartIDNotInAndProbIDNotIn(partIdAlready, probIdAlready));

			additionalProbMap = minitestInfo.getProbMap();
			partNumOrderMap = minitestInfo.getNumOrderMap();

			// 함수화로 대체된 코드 (테스트 후 삭제 예정)
			// for (TestProblem probInfo : minitestRepo.findAllByPartPartIDNotInAndProbIDNotIn(partIdAlready, probIdAlready)) {
			// 	// 파트 정보 없을 경우.
			// 	if (probInfo.getPart() == null) {
			// 		logger.info("No part mapping info for probId : " + Integer.toString(probInfo.getProbID()) + ", DB data issue.\nPlease check if data is filled in 'PART' table");
			// 		continue;
			// 	}

			// 	// 파트 별 문제 수집
			// 	if (!additionalProbMap.containsKey(probInfo.getPart().getPartID())) {
			// 		additionalProbMap.put(probInfo.getPart().getPartID(), Arrays.asList(probInfo.getProbID()));
			// 	} else {
			// 		additionalProbMap.get(probInfo.getPart().getPartID()).add(probInfo.getProbID());
			// 	}

			// 	// 파트 별 [파트 순서, 출제 문제 개수] 없었던 정보 있으면 추가해줌.
			// 	if (!partNumOrderMap.containsKey(probInfo.getPart().getPartID())){
			// 		partNumOrderMap.put(probInfo.getPart().getPartID(), Arrays.asList(probInfo.getPart().getOrderNum(), probInfo.getPart().getProblemCount()));
			// 	}
			// }

			// 추가로 조회해온 문제들로 부족한 파트의 문제 수 보충.
			for (Integer partId : additionalProbMap.keySet()) {
				if (partProbIdMap.keySet().contains(partId)) {			// 해당 파트에 문제가 일부 있는 상태라면, 해당 파트에 모자라는 수 만큼 문제 넣어줌.
					if (partProbIdMap.get(partId).size() < partNumOrderMap.get(partId).get(1)) {
						Collections.shuffle(additionalProbMap.get(partId));
						for (int i=0; i < partNumOrderMap.get(partId).get(1) - partProbIdMap.get(partId).size(); i++) {
							partProbIdMap.get(partId).add(additionalProbMap.get(partId).get(i));
						}
					}
				} else {												// 해당 파트 문제가 아예 없는 상태라면, 파트 추가하여 정해진 숫자만큼 다 넣어줌.
					Collections.shuffle(additionalProbMap.get(partId));
					partProbIdMap.put(partId, new ArrayList<Integer>());
					for (int i=0; i < partNumOrderMap.get(partId).get(1); i++) {
						partProbIdMap.get(partId).add(additionalProbMap.get(partId).get(i));
					}
				}
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
		map.put("minitestProblems", minitestProblems);
		return map;
	}

	private MinitestInfo parseMinitestQueryResult(Map<Integer, List<Integer>> probMap, Map<Integer, List<Integer>> numOrderMap, List<TestProblem> queryResult) {
		MinitestInfo minitestInfo = new MinitestInfo();

		for (TestProblem problem : queryResult) {
			if (problem.getPart() == null) {
				logger.info("No part mapping info for probId : " + Integer.toString(problem.getProbID()) + ", DB data issue.\nPlease check if data is filled in 'PART' table");
				continue;
			}
			// 파트 별 문제 수집
			if (!probMap.containsKey(problem.getPart().getPartID())) {
				probMap.put(problem.getPart().getPartID(), Arrays.asList(problem.getProbID()));
			} else {
				probMap.get(problem.getPart().getPartID()).add(problem.getProbID());
			}

			// 파트 별 [파트 순서, 출제 문제 개수] 수집
			if (!numOrderMap.containsKey(problem.getPart().getPartID())){
				numOrderMap.put(problem.getPart().getPartID(), Arrays.asList(problem.getPart().getOrderNum(), problem.getPart().getProblemCount()));
			}
		}

		minitestInfo.set(probMap, numOrderMap);
		return minitestInfo;
	}
}
