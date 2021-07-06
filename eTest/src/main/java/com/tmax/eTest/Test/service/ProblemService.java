package com.tmax.eTest.Test.service;

import java.util.ArrayList;
import java.util.Random;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static java.util.stream.Collectors.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tmax.eTest.Contents.model.DiagnosisCurriculum;
import com.tmax.eTest.Contents.model.DiagnosisProblem;
import com.tmax.eTest.Contents.model.TestProblem;
import com.tmax.eTest.Contents.repository.DiagnosisCurriculumRepository;
import com.tmax.eTest.Contents.repository.DiagnosisProblemRepository;
import com.tmax.eTest.Contents.repository.TestProblemRepository;

@Service
public class ProblemService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

	@Autowired
	DiagnosisProblemRepository diagnosisRepo;

	@Autowired
	DiagnosisCurriculumRepository diagnosisCurriculumRepo;

	@Autowired
	TestProblemRepository minitestRepo;

	public Map<String, Object> getDiagnosisTendencyProblems() {
		Map<String, Object> map = new HashMap<String, Object>();
		String type = "성향";

		logger.info("Getting all diagnosis tendency problem info......");
		// 진단 커리큘럼 테이블에서 type에 해당되는 커리큘럼 아이디를 모두 가져옴
		List<DiagnosisCurriculum> selectedCurriculum = diagnosisCurriculumRepo.findByChapter(type);
		List<Integer> selectedCurriculumId = selectedCurriculum.stream().map(dc -> dc.getCurriculumId())
				.collect(toList());
		logger.info(String.format("Retrieved all curriculum ids with type %s", type));

		// 성향문제의 경우 : 가져온 커리큘럼 아이디에 해당되는 모든 진단 문제를 하나의 리스트에 합침
		List<Integer> problemLists = new ArrayList<Integer>();
		String choice = Arrays.asList("A", "B", "C").get(new Random().nextInt(3));
		for (Integer i : selectedCurriculumId) {
			List<DiagnosisProblem> selectedProblems = diagnosisRepo.findByCurriculumId(i);
			List<Integer> selectedProbIds = new ArrayList<Integer>();
			if (i < 7) {
				selectedProbIds = selectedProblems.stream().map(sp -> sp.getProbId()).collect(toList());
			} else {
				selectedProbIds = selectedProblems.stream().filter(sp -> sp.getSetType().equals(choice))
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

		logger.info("Getting all diagnosis tendency problem info......");
		// 진단 커리큘럼 테이블에서 type에 해당되는 커리큘럼 아이디를 모두 가져옴
		List<DiagnosisCurriculum> selectedCurriculum = diagnosisCurriculumRepo.findByChapter(type);
		List<Integer> selectedCurriculumId = selectedCurriculum.stream().map(dc -> dc.getCurriculumId())
				.collect(toList());
		logger.info(String.format("Retrieved all curriculum ids with type %s", type));

		// 지식문제의 경우 : 가져온 커리큘럼 아이디에 해당되는 진단문제 리스트를 리스트 안에 넣음
		List<List<Integer>> problemLists = new ArrayList<List<Integer>>();
		for (Integer i : selectedCurriculumId) {
			List<DiagnosisProblem> selectedProblems = diagnosisRepo.findByCurriculumId(i);
			List<String> selectedSubjects = selectedProblems.stream().map(sp -> sp.getSubject()).collect(toList());
			List<Integer> selectedProbIds = selectedProblems.stream().map(sp -> sp.getProbId()).collect(toList());
			selectedSubjects.add("dummy"); // 마지막을 찾기 위한 더미 tag 입력
			List<Integer> tmpList = new ArrayList<Integer>();
			for (int j = 0; j < selectedProbIds.size(); j++) {
				String tag = selectedSubjects.get(j);
				tmpList.add(selectedProbIds.get(j));
				// 태그가 달라지면 한번 넣기
				if (!tag.equals(selectedSubjects.get(j + 1))) {
					problemLists.add(new ArrayList<Integer>(tmpList));
					tmpList.clear();
				}
			}
		}
		map.put("knowledgeProblems", problemLists);

		return map;
	}

	public Map<String, Object> getMinitestProblems(Integer set_num) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (set_num == null || set_num == 0) {
			// 미니테스트 세트의 개수가 자주 변한다면 (지속적으로 추가된다면)
			Integer max_setNum = minitestRepo.findMaximumSetNum();
			if (max_setNum == 0 || max_setNum == null) {
				map.put("error", "No minitest problems in DB");
				return map;
			}
			List<Integer> setNumList = new ArrayList<Integer>();
			for (int i = 0; i < max_setNum; i++) {
				setNumList.add(i + 1);
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

			if (map.containsKey("error"))
				map.replace("error", map.get("error") + "\n" + err_msg);
			else
				map.put("error", err_msg);
		}

		map.put("minitestProblems", minitestProblems);

		return map;
	}
}
