package com.tmax.eTest.Test.service;

import java.util.ArrayList;
import java.util.UUID;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.*;

import org.json.simple.parser.ParseException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import com.tmax.eTest.Common.model.problem.DiagnosisCurriculum;
import com.tmax.eTest.Common.model.problem.DiagnosisProblem;
import com.tmax.eTest.Common.model.problem.Problem;
import com.tmax.eTest.Common.model.problem.TestProblem;
import com.tmax.eTest.Contents.repository.DiagnosisCurriculumRepository;
import com.tmax.eTest.Contents.repository.DiagnosisProblemRepository;
import com.tmax.eTest.Contents.repository.ProblemRepository;
import com.tmax.eTest.Contents.repository.TestProblemRepository;
import com.tmax.eTest.LRS.dto.GetStatementInfoDTO;
import com.tmax.eTest.LRS.dto.StatementDTO;
import com.tmax.eTest.LRS.util.LRSAPIManager;
import com.tmax.eTest.Test.util.MinitestPart;

@Slf4j
@Service("ProblemServiceV1")
@Primary
public class ProblemServiceV1 implements ProblemServiceBase {

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

		log.info("Getting diagnosis curriculums info for " + type + "......");
		// 진단 커리큘럼 테이블에서 type에 해당되는 커리큘럼 아이디를 모두 가져옴
		List<DiagnosisCurriculum> selectedCurriculum = diagnosisCurriculumRepo.findByChapterOrderByCurriculumIdAsc(type);
		
		List<Integer> selectedCurriculumId = selectedCurriculum.stream().map(dc -> dc.getCurriculumId())
				.collect(toList());
		log.info(String.format("Retrieved all curriculum ids with type %s", type));

		// 성향문제의 경우 : 가져온 커리큘럼 아이디에 해당되는 모든 진단 문제를 하나의 리스트에 합침
		List<Integer> problemLists = new ArrayList<Integer>();

		for (Integer i : selectedCurriculumId) {
			List<DiagnosisProblem> selectedProblems = diagnosisRepo.findByCurriculumIdOrderByOrderNumAsc(i);
			List<Integer> selectedProbIds = new ArrayList<Integer>();
			selectedProbIds = selectedProblems.stream().map(sp -> sp.getProbId()).collect(toList());
			problemLists.addAll(selectedProbIds);
		}
		
		map.put("tendencyProblems", problemLists); // [1,2,3,4,5,6,7,8,9]의 형태
		log.info(map.toString());

		return map;
	}

	public Map<String, Object> getDiagnosisKnowledgeProblems() {
		Map<String, Object> map = new HashMap<String, Object>();
		String type = "지식";
		
		log.info("Getting knowledge diagnosis problems info......");
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

			// 마지막 문제까지 돌았으면 마지막 주제에 대해서 담기
			if (p.getProbID() == selectedProblems2.get(selectedProblems2.size()-1).getProbID()) {
				List<Integer> tempList = new ArrayList<Integer>();
				if (difficultyList.contains("상"))
					tempList.add(probIdList.get(difficultyList.indexOf("상")));
				if (difficultyList.contains("중"))
					tempList.add(probIdList.get(difficultyList.indexOf("중")));
				if (difficultyList.contains("하"))
					tempList.add(probIdList.get(difficultyList.indexOf("하")));
				
				problemList.add(tempList);
			}
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

	public Map<String, Object> getMinitestProblems(String userId) {
		final Integer TOTAL_PART_NUM = 5;
		final Integer TOTAL_PROB_NUM = 20;

		// outputs
		Map<String, Object> map = new HashMap<String, Object>();
		String newProbSetId = UUID.randomUUID().toString();
		List<Integer> minitestProblems = new ArrayList<Integer>();
		String continueProbSetId = "";
		List<Integer> continueProblems = new ArrayList<Integer>();
		List<String> continueAnswers = new ArrayList<String>();
		List<Integer> isCorrect = new ArrayList<Integer>();
		Integer guessAlarm = 0;
		
		/*
		 *	> LRS에서 문제 풀이 이력 참고해 중복 방지.
		 *	[구현완료]
		*/

		List<StatementDTO> statementQuery = new ArrayList<StatementDTO>();
		GetStatementInfoDTO statementInput = new GetStatementInfoDTO();
		statementInput.pushUserId(userId);
		statementInput.pushSourceType("mini_test_question");
		statementInput.pushActionType("submit");

		// LRS statement 조회
		log.info("Getting LRS log data......");
		try {
			// Ordering? --> Order by Timestamp ASC
			statementQuery = lrsAPIManager.getStatementList(statementInput);
			log.info("length of the statement query result : " + Integer.toString(statementQuery.size()));
			Collections.reverse(statementQuery);		// 최근 부터 set id 탐색해야 하므로, reverse
		} catch (java.text.ParseException e) {
			log.info("error : ParseException occurred.");
			map.put("error", "ParseException occurred.");
			return map;
		}

		/*
		 *	8/12 (목) : LRS 데이터에서 가장 최근 시험 본 미니진단에서 20문제 모두 풀었는지 확인해서, 다 못 풀었으면 해당 세트 ID에 해당하는 문제 모두 불러오기
		 *	각 푼 문제가 어떤 파트의 문제인지 & 어떤 답을 답했는지 DB에서 조회해서 미리 문제 수집하는 map이나 리턴값에 넣어놓기
		 *	[구현완료]
		*/
		
		List<Integer> solvedProbList = new ArrayList<Integer>();			// 푼 문제 리스트
		
		JSONParser parser = new JSONParser();
		JSONObject json = new JSONObject();
		
		if (statementQuery.size() > 0)
			log.info("first row of query result : " + statementQuery.get(0).toString());
		else
			log.info("Result of LRS submit statement query for ID " + userId +" is empty");
		
		// LRS statement 리스트에서 푼 문제 내역과 가장 최근 푼 미니진단 세트 정보 수집
		int isLatest = 0;		// 가장 최근 푼 세트 id 인지 아닌지 여부

		for (StatementDTO query : statementQuery) {
			int sourceId = Integer.parseInt(query.getSourceId());			// 문제 ID
			String extension = query.getExtension();						// extension 컬럼 정보
			if (isLatest < 2 && extension != null) {											// 최근 푼 문제 세트 범위이면서, extension이 null 이 아니라면
				if (extension.startsWith("{") && extension.endsWith("}")) {						// json 포맷이 맞다면
					try {
						json = (JSONObject) parser.parse(extension);
						if (json.containsKey("diagProbSetId")) {
							String diagProbSetId = json.get("diagProbSetId").toString();		// extension 컬럼에 진단 세트 id 존재
							// 가장 최근 푼 세트 ID 수집
							if (isLatest == 0) {
								if (!diagProbSetId.equalsIgnoreCase("")) {
									continueProbSetId = json.get("diagProbSetId").toString();
									log.info("Latest set id : " + continueProbSetId);
									isLatest++;
								}
							}
				
							// 최근 푼 세트에 해당하는 문제 풀이 정보 저장 (문제 / 유저 답 / 정답여부)
							if (isLatest == 1) {
								if (diagProbSetId.equalsIgnoreCase(continueProbSetId)) {
									// 오류로 인한 LRS 중복 기록 있을 수 있음.
									if (!continueProblems.contains(sourceId)){
										continueProblems.add(sourceId);
										isCorrect.add(query.getIsCorrect());
										continueAnswers.add(query.getUserAnswer());
									}
								} else isLatest++;
							}
						} else {
							log.info("The key 'diagProbSetId' does not exist in extension json : " + json.toString());
						}

						// cumulate guessAlarm value
						if (json.containsKey("guessAlarm")) {
							if (json.get("guessAlarm").toString().equalsIgnoreCase("1")) {
								guessAlarm++;
							}
						}
					} catch (ParseException e) {
						log.info("Extension ParseException with String Value : " + extension.toString());
					}
				} else log.info("Json format, which is surrounded by '{' and '}', NOT found in extension : " + query.getExtension().toString());
			}

			// 중복 방지 용 푼 문제 ID 수집
			if (!solvedProbList.contains(sourceId))
				solvedProbList.add(sourceId);
		}

		/* 
		 * 이어풀기 존재 여부 판단.
		 * [구현완료]
		*/

		if (continueProblems.size() >= TOTAL_PROB_NUM) {			// 중복 배제하여 수집한 최근 세트 문제 수가 세트당 최대 문제 개수 이상이면 이어풀기 X.
			isCorrect.clear();
			continueAnswers.clear();
			guessAlarm = 0;
		} else {
			Collections.reverse(continueProblems);
			Collections.reverse(isCorrect);
			Collections.reverse(continueAnswers);
		}

		/*
		 *	중복 배제하여 미니진단 문제 조회 - (중복 배제하여) 전체 문항 조회 후 파트 별 개수에 맞게 분배
		 *	[구현완료]
		*/

		log.info("Getting minitest problem infos......");
		if (solvedProbList.size() == 0)						// LRS에 푼 기록이 없는 처음 진입한 미니진단 유저 : 존재하지 않는 문제 id 넣어줌
			solvedProbList = Arrays.asList(-1);
		List<TestProblem> minitestQueryResult = minitestRepo.findAllByProbIDNotIn(solvedProbList);			// Not in (리스트)에 빈 리스트 들어가면 조회 X.

		/* 8/27
		 * 문제를 다 풀어본 수준이 아니라, 그 이후에도 계속해서 꾸준히 문제를 반복해서 푸는 유저의 경우 : 문제 풀이 frequency 고려해줘야 함.
		 * -> 추후 개발 요건.
		*/

		// 모든 문제 다 풀었으면 solvedProbList가 DB의 모든 probId 리스트와 동일. 위 쿼리 결과 size() = 0
		// -> 모든 풀에서 문제 출제해야 함.
		if (minitestQueryResult.size() == 0) {
			minitestQueryResult = minitestRepo.findAll();
		}
		if (minitestQueryResult.size() > 0) log.info("Selected minitest probs size : " + Integer.toString(minitestQueryResult.size()));

		/* 
		 * 쿼리해온 미니진단 문제 정보 파싱
		 * [구현완료]
		*/

		Map<Integer, List<Integer>> partProbIdMap = new HashMap<Integer, List<Integer>>();		// 각 파트 별로 문제 ID 수집
		Map<Integer, List<Integer>> partNumOrderMap = new HashMap<Integer, List<Integer>>();	// 각 파트의 [순서, 출제 문제 개수] 정보 저장.

		MinitestInfo minitestInfo = parseMinitestQueryResult(partProbIdMap, partNumOrderMap, minitestQueryResult);

		// 쿼리 해온 미니테스트 문제 정보 - 파트 별 문제 ID map & 각 파트 별 순서 & 문제 개수 map
		partProbIdMap = minitestInfo.getProbMap();					// {part1: [11, 12, 13, 14, ...], part2: [...], ... }
		partNumOrderMap = minitestInfo.getNumOrderMap();			// {part1: [1, 4], part2: [2, 3], part3: [3, 5], ...} -> part1: ['order', '# of prob']

		/*
		 *	8/6 추가 개발 필요 사항. : 사용자가 많이 풀어서 DB에 더 이상 중복 배제 남은 문제 없을 경우.
		 *	모든 파트(5개)가 있는지 확인

		 *	파트의 문제 개수가 설정된 [출제 문제 개수] 이상 존재하는지 확인

		 *	(아예 없는 파트 + 문제 개수 부족한 파트) 에 해당하는 전체 문제 (중복 허용) 다시 DB 조회해와서 partProbIdMap과 partNumOrderMap에 추가해줘야 됨.
		 *	쿼리 할 때 : WHERE PART_ID NOT IN (현재 조건을 다 충족하는 파트들 ID)

		 *	[구현완료]
		*/

		/*
		 *	파트 별 출제 문제 개수 만족한 파트 id 수집
		 *	문제 개수 부족한 파트의 이미 수집된 문제 id 수집
		 *	[구현완료]
		*/
		List<Integer> partIdAlready = new ArrayList<Integer>();
		List<Integer> probIdAlready = new ArrayList<Integer>();

		Map<Integer, List<Integer>> unsatisfiedPartProbMap = new HashMap<Integer, List<Integer>>();
		for (Integer partId : partProbIdMap.keySet()) {
			if (partProbIdMap.get(partId).size() < partNumOrderMap.get(partId).get(1)){
				for (Integer probId : partProbIdMap.get(partId)) {
					probIdAlready.add(probId);
				}
				unsatisfiedPartProbMap.put(partId, partProbIdMap.get(partId));
			} else if (!partIdAlready.contains(partId)) partIdAlready.add(partId);				// 필요한 문제 개수 충족한 파트만 partIdAlready 에 저장.
		}

		/*
		 *	이미 푼 문제 제외하고 조회했는데 필요한 개수 충족 X. --> 현재 가져와놓은 문제 외에는 모두 풀어봤단 의미이므로, 가져온 문제 제외하고 그냥 전체에서 랜덤 출제.
		 *	[구현완료]
		 *	전체 한 번 이상 다 푼 사람에 대한 추가 디테일한 로직 구상은 추후에.
		*/

		Map<Integer, List<Integer>> additionalProbMap;
		List<TestProblem> queryList = new ArrayList<TestProblem>();
		if (partIdAlready.size() < TOTAL_PART_NUM){
			// log.info("Only " + Integer.toString(partIdAlready.size()) + " parts are satisfied : " + partIdAlready.toString() + ", " + probIdAlready.toString());
			
			// 문제 개수 부족한 파트 존재하면, DB 추가 서치.
			log.info("Getting supplementary minitest problem infos......");
			if (probIdAlready.size() == 0)
				probIdAlready = Arrays.asList(-1);
			queryList = minitestRepo.findAllByPartPartIDNotInAndProbIDNotIn(partIdAlready, probIdAlready);

			// -> 문제 개수 총족한 파트(partIdAlready) 제외 나머지 파트에서 이미 조금 수집해놓은 문제(probIdAlready) 빼고 전체 문제 서치
			// -> 부족할 수가 없음. 만약 부족하다면, 해당 파트 원래 문제 개수가 부족한 것. (5문제 출제해야되는데 총 4문제 이하 밖에 없는 것.)
			
			// 부족하다고 판단돼서 더 가져왔다면,
			// 쿼리 결과를 각 파트 별로 문제 분리. 새로 조회된 파트는 파트 순서 & 문제 수 정보도 수집.
			if (queryList != null) {
				additionalProbMap = new HashMap<Integer, List<Integer>>();
				minitestInfo = parseMinitestQueryResult(additionalProbMap, partNumOrderMap, queryList);
				additionalProbMap = minitestInfo.getProbMap();
				partNumOrderMap = minitestInfo.getNumOrderMap();
		
				// 추가로 조회해온 문제들로 부족한 파트의 문제 수 보충.
				for (Integer partId : additionalProbMap.keySet()) {
					Integer threshold = partNumOrderMap.get(partId).get(1);
					if (partProbIdMap.keySet().contains(partId)) {			// 해당 파트에 문제가 일부 있는 상태라면, 해당 파트에 모자라는 수 만큼 문제 넣어줌.
						if (partProbIdMap.get(partId).size() < threshold) {
							Collections.shuffle(additionalProbMap.get(partId));
	
							for (Integer probId : additionalProbMap.get(partId)) {
								if (!partProbIdMap.get(partId).contains(probId)) {
									partProbIdMap.get(partId).add(probId);
									if (partProbIdMap.get(partId).size() == threshold) break;
								}
							}
						}
					} else {												// 해당 파트 문제가 아예 없는 상태라면, 파트 추가하여 정해진 숫자만큼 다 넣어줌.
						Collections.shuffle(additionalProbMap.get(partId));
						partProbIdMap.put(partId, new ArrayList<Integer>());
						for (int i=0; i < threshold; i++) {
							partProbIdMap.get(partId).add(additionalProbMap.get(partId).get(i));
						}
					}
				}
			}		
		} // partIdAlready는 문제 개수 완벽히 충족한 파트에 대해서만 수집하므로, partIdAlready.size() == TOTAL_PART_NUM 이면 문제가 모두 수집된 상태.
		
		/*
		 *	파트 순서로 정렬
		 *	[구현완료]
		*/

		List<Map.Entry<Integer, List<Integer>>> orderedList = new ArrayList<Map.Entry<Integer, List<Integer>>>(partNumOrderMap.entrySet());
		Collections.sort(orderedList, new Comparator<HashMap.Entry<Integer, List<Integer>>>(){
			@Override
			public int compare(HashMap.Entry<Integer, List<Integer>> o1, HashMap.Entry<Integer, List<Integer>> o2) {
				return o1.getValue().get(0).compareTo(o2.getValue().get(0));
			}
		});

		/*
		 *	순서대로 각 파트 별 문제 리스트에서 설정된 개수 만큼 랜덤으로 선택하여 수집 (minitestProblems : 새 미니진단 문제들)
		 *	[구현완료]
		*/

		Integer delimiter = 0;		// 이어풀기 용
		Integer idx = 0;
		Integer lastPartOrder = -1;
		Integer deficitNum = 0;
		for (Map.Entry<Integer, List<Integer>> part : orderedList) {
			int partId = part.getKey();
			int thres = part.getValue().get(1);
			List<Integer> probIdList = partProbIdMap.get(partId);
			Collections.shuffle(probIdList);

			for (int i=0 ; i < thres ; i++) {					// 안전 장치로 파트 별 문제 개수 다시 고려. (수집할 때 문제 개수 잘 고려됐다면, 그냥 리스트 그대로 넣어도 됨)
				if (probIdList.size() > i) {
					minitestProblems.add(probIdList.get(i));
				}
			}

			// 이어풀기 문제가 존재할 경우에도 추가. (이어풀기도 푼문제에 포함돼서 배제되었으므로, 혹은 남은 문제 없으면 전체 pool에서 가져왔음)
			delimiter += thres;
			if (continueProblems.size() > 0) {
				if (lastPartOrder == -1) {
					if (continueProblems.size() < delimiter) {
						lastPartOrder = idx;
						deficitNum = delimiter - continueProblems.size();			// 미니진단 문제가 파트 별로 순서대로 출제되기 때문에, 문제 수 부족한 파트가 존재한다면 1개만 존재. : 부족한 문제 수 = deficitNum
					}
				}
			}
			idx++;
		}

		if (lastPartOrder == -1) {								// 그럴리가 없지만 안전 장치로 (처음에 continueProblems 20개 이상이면 clear)
			lastPartOrder = partProbIdMap.size() - 1;
		}

		/*
		 *	이어풀기 세트 이미 푼 문제 정보 이후에 필요한 만큼 추가
		 *	[구현완료]
		*/

		for (int order = lastPartOrder; order <= partProbIdMap.size() - 1; order++) {				// 파트 별로 loop
			int thres = orderedList.get(order).getValue().get(1);			// 해당 파트 필요한 문제 개수
			if (order == lastPartOrder) {
				if (deficitNum == 0)			// 마지막으로 푼 파트의 문제가 다 충족됐다면, continue
					continue;
				else
					thres = deficitNum;
			}

			List<Integer> probIdList = partProbIdMap.get(orderedList.get(order).getKey());
			Collections.shuffle(probIdList);

			int count = 0;
			int index = 0;

			while (count < thres) {
				if (!continueProblems.contains(probIdList.get(index))) {			// 중복되지 않게 이어풀기 리스트에 추가.
					continueProblems.add(probIdList.get(index));
					count++;
				}
				index++;
			}
		}

		log.info("> minitest logic end! : " + Integer.toString(minitestProblems.size()) + ", " + minitestProblems.toString() + " / " + Integer.toString(continueProblems.size()) + ", " + continueProblems.toString());

		// output 생성

		map.put("newProbSetId", newProbSetId);
		map.put("minitestProblems", minitestProblems);
		if (continueProblems.size() > 0) {
			map.put("continueProbSetId", continueProbSetId);
		}
		map.put("continueProblems", continueProblems);
		map.put("continueAnswers", continueAnswers);
		map.put("isCorrect", isCorrect);
		map.put("guessAlarm", guessAlarm);
		
		return map;
	}

	private MinitestInfo parseMinitestQueryResult(Map<Integer, List<Integer>> probMap, Map<Integer, List<Integer>> numOrderMap, List<TestProblem> queryResult) {
		MinitestInfo minitestInfo = new MinitestInfo();
		List<Integer> partIdList = MinitestPart.minitestPartIdList;

		for (TestProblem problem : queryResult) {
			int partId = problem.getPartID();
			if (!partIdList.contains(partId))
				continue;

			if (problem.getPart() == null) {
				log.info("No part mapping info for probId : " + Integer.toString(problem.getProbID()) + ", DB data issue.\nPlease check if data is filled in 'PART' table");
				continue;
			}
			// 파트 별 문제 수집
			if (!probMap.containsKey(partId)) {
				probMap.put(partId, new ArrayList<Integer>(Arrays.asList(problem.getProbID())));
			} else {
				probMap.get(partId).add(problem.getProbID());
			}

			// 파트 별 [파트 순서, 출제 문제 개수] 수집
			if (!numOrderMap.containsKey(partId)){
				numOrderMap.put(partId, Arrays.asList(problem.getPart().getOrderNum(), problem.getPart().getProblemCount()));
			}
		}

		minitestInfo.set(probMap, numOrderMap);
		return minitestInfo;
	}

	// Not using it
	public Map<String, Object> getMinitestProblemsV0(Integer userId){
		return null;
	}

}
