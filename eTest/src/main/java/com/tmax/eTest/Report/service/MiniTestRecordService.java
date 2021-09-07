package com.tmax.eTest.Report.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import com.tmax.eTest.Contents.repository.ProblemRepository;
import com.tmax.eTest.LRS.util.LRSAPIManager;
import com.tmax.eTest.Report.dto.MiniTestRecordDTO;
import com.tmax.eTest.Report.util.SNDCalculator;
import com.tmax.eTest.Report.util.minitest.PartMapper;
import com.tmax.eTest.Common.repository.report.MinitestReportRepo;

import com.tmax.eTest.Common.model.report.MinitestReport;
import com.tmax.eTest.Report.dto.minitest.PartInfoDTO;
import com.tmax.eTest.Report.dto.minitest.PartDataDTO;

import com.tmax.eTest.Common.repository.uk.UkMasterRepo;
import com.tmax.eTest.Common.model.uk.UkMaster;

import java.util.Optional;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.List;
import java.util.ArrayList;
import java.time.ZonedDateTime;
import java.util.Set;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;

import java.lang.reflect.Type;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonArray;
import java.util.stream.StreamSupport;

import com.tmax.eTest.LRS.dto.GetStatementInfoDTO;
import com.tmax.eTest.LRS.dto.StatementDTO;

import com.tmax.eTest.Common.model.problem.Problem;


@Data
@AllArgsConstructor
class PartDataTuple{
	private String key;
	private PartDataDTO partData;
}

@Service
@Slf4j
public class MiniTestRecordService {

	@Autowired private LRSAPIManager lrsAPIManager;
	// @Autowired private TritonAPIManager tritonAPIManager;
	
	@Autowired private ProblemRepository problemRepo;
	// @Autowired private UserKnowledgeRepository userKnowledgeRepo;
	// @Autowired private DiagnosisReportRepo diagnosisReportRepo;
	
	// @Autowired private StateAndProbProcess stateAndProbProcess;
	// @Autowired private RuleBaseScoreCalculator ruleBaseScoreCalculator;
	// @Autowired private UKScoreCalculator ukScoreCalculator;
	// @Autowired private DiagnosisComment commentGenerator;
	@Autowired private SNDCalculator sndCalculator;

	@Autowired private MinitestReportRepo reportRepo;

	@Autowired private UkMasterRepo ukMasterRepo;


	private static final int PICK_ALARM_CNT_THRESHOLD = 3;
	private static final int COMMENT_HIGH_LOW_SPLIT_THRESHOLD_SCORE = 60;

	private PartDataDTO buildPartData(JsonArray mastery, String partname){
		PartDataDTO output = buildPartData(mastery);
		output.setPartName(partname);
		return output;
	}

	private PartDataDTO buildPartData(JsonArray mastery){
		//Parse raw data
		Type typeLLS = new TypeToken< List<List<String>> >(){}.getType();
		List<List<String>> ukDataList = null;
		try{ukDataList =  new Gson().fromJson(mastery, typeLLS);}
		catch(Exception e){e.printStackTrace(); log.error("mastery parse error {}", mastery.toString()); return null;}

		//Filter invalid entries
		ukDataList = ukDataList.stream().filter(data -> data.size() == 3).collect(Collectors.toList());

		//Get all ukIdList
		Set<Integer> ukIdSet = ukDataList.stream().parallel()
										 .flatMap(data -> {
											 try{ return Stream.of(Integer.parseInt(data.get(0)));}
											 catch(Exception e){e.printStackTrace(); log.error("uk id parse error {}", data.toString()); return Stream.empty();}
										 }).collect(Collectors.toSet());
		Map<Integer, UkMaster> ukDetailMap = ukMasterRepo.findAllById(ukIdSet).stream()
														 .collect(Collectors.toMap(UkMaster::getUkId, uk -> uk));

		//For each uk --> try parse score + build ukInfo
		double totalScore = ukDataList.stream()
				.flatMap(data -> {
					//3rd element is score --> try float conversion
					try{return Stream.of( Double.parseDouble(data.get(2)) );}
					catch(Exception e){log.error("score parse error {}", data); return Stream.empty();}
				}).reduce(0.0, Double::sum);
		
		List<List<String>> ukInfo = ukDataList.stream().parallel()
											  .flatMap(data -> {
													Integer ukId = null;
													try{ukId = Integer.parseInt(data.get(0));}
													catch(Exception e){log.warn("ukid parse error. {}", data.toString());return Stream.empty();}

													UkMaster uk = ukDetailMap.get(ukId);
													if(uk == null)
														return Stream.empty();
													
													//build info (name, score, desc, link)	
													return Stream.of( Arrays.asList(uk.getUkName(), data.get(2), uk.getUkDescription(), uk.getExternalLink() ) );
											  }).collect(Collectors.toList());

		long score = (long)Math.floor(totalScore / ukDataList.size());

		return PartDataDTO.builder()
							.score(score).ukInfo(ukInfo)
							.percentage((long)sndCalculator.calculateForMiniTest((double)score))
							.build();
	}

	private List<List<String>> createProblemInfo(List<StatementDTO> statementList, String userId){
		//Order lrs by time
		try{
			Collections.sort(statementList, (a,b)->ZonedDateTime.parse(a.getTimestamp())
												.compareTo(ZonedDateTime.parse(b.getTimestamp())) );
		}
		catch(Exception e){log.error("Timestamp sort failed. will use unsorted lrs list");}

		//Find all prob info in lrs
		Set<Integer> probIdSet = statementList.stream().parallel()
									.map(StatementDTO::getSourceId)
									.flatMap(strId -> {
										try {return Stream.of(Integer.parseInt(strId));}
										catch(Exception e){return Stream.empty();} 
									})
									.collect(Collectors.toSet());
		Map<Integer, Problem> probMap = problemRepo.findAllById(probIdSet).stream().parallel()
											.filter(prob -> prob.getDifficulty() != null)
											.collect(Collectors.toMap(Problem::getProbID, p->p));
		
		//Index. TODO. parallelize here to make dramatic speed change
		AtomicInteger probIndex = new AtomicInteger(1);
		List<List<String>> outProbInfo = statementList.stream()
												  .flatMap(statement -> {
														Integer probId = null;
														try{probId = Integer.parseInt(statement.getSourceId());}
														catch(Exception e){return Stream.empty();}

														//get question test from question column and diff data
														Problem prob = probMap.get(probId);
														if(prob == null){
															log.warn("Prob {} not found skipping info build for this Q", probId);
															return Stream.empty();
														}

														//Get data with type "QUESTION_TEXT"
														JsonArray questionDataList = null; 
														try {questionDataList = JsonParser.parseString(prob.getQuestion()).getAsJsonArray();}
														catch(Exception e){log.error("question data json invalid for {}", probId); return Stream.empty();}

														List<String> quesTxtList = 
																StreamSupport.stream(questionDataList.spliterator(), false)
																	 .filter(elem -> {
																		 String type = ((JsonObject)elem).get("type").getAsString();
																		 if(type != null && type.equals("QUESTION_TEXT"))
																		 	return true;

																		 return false;
																	 })
																	 .map(elem -> ((JsonObject)elem).get("data").getAsString())
																	 .collect(Collectors.toList());

													  	//[idx, probid, correct(true/false), problem context, diff]
														return Stream.of(Arrays.asList(Integer.toString(probIndex.getAndIncrement()),
																					   probId.toString(),
																					   statement.getIsCorrect() == 1 ? "true": "false",
																					   quesTxtList.size() > 0 ? quesTxtList.get(0) : null,
																					   prob.getDifficulty()
																					   ));
												  })
												  .collect(Collectors.toList());

		//Add index (TEMP)

		return outProbInfo;
	}

	private List<StatementDTO> filterPureLrsStatement(List<StatementDTO> statementList) {
		Map<String, StatementDTO> srcIdStatementMap = new HashMap<>();
		for(StatementDTO statement : statementList){
			String srcId = statement.getSourceId();
			//if not in map insert
			if(!srcIdStatementMap.containsKey(srcId)){
				srcIdStatementMap.put(srcId, statement);
				continue;
			}
			
			//if exists, compare the timestamp and add the later one
			ZonedDateTime inMapTime = null;
			ZonedDateTime currTime = null;
			//Try and inMap's time and curr time
			try{inMapTime = ZonedDateTime.parse(srcIdStatementMap.get(srcId).getTimestamp());}
			catch(Exception e){log.warn("Timestamp error in LRS statement {}", srcIdStatementMap.get(srcId).getTimestamp());}
			try{currTime = ZonedDateTime.parse(statement.getTimestamp());}
			catch(Exception e){log.warn("Timestamp error in LRS statement {}", statement.getTimestamp());}

			//Overwrite with value that has time
			if(inMapTime == null && currTime != null){
				srcIdStatementMap.put(srcId, statement);
				continue;
			}


			//if both is not null
			if(inMapTime != null && currTime != null){
				if(inMapTime.compareTo(currTime) > 0)
					srcIdStatementMap.put(srcId, statement);

				continue;
			}

			//Other case?
			log.warn("Too many invalid timestamps in lrs");
		}

		//return list from map
		List<StatementDTO> filteredList = srcIdStatementMap.entrySet().stream().map(e -> e.getValue()).collect(Collectors.toList());
		// Collections.sort(filteredList, ) --> no need to sort. there is sorting later on
		
		return filteredList;
	}

	
	public MiniTestRecordDTO getMiniTestRecord(String userId,String probSetId){
		//Get report
		Optional<MinitestReport> queryResult = reportRepo.getReport(probSetId, userId);
		if(!queryResult.isPresent()){return null;}
		MinitestReport report = queryResult.get();
		

		//Build part info from minitestUkMastery field
		PartInfoDTO partInfo = new PartInfoDTO();
		Map<String, PartDataDTO> partInfoReadable = null;
		String minitestMastery = report.getMinitestUkMastery();
		if(minitestMastery != null){
			try {
				//Parse the string to json object
				JsonObject masteryMap = (JsonObject)JsonParser.parseString(minitestMastery);

				//Make readable map first
				// partInfoReadable = masteryMap.entrySet().stream()
				// 					.collect(Collectors.toMap(e -> e.getKey(),
				// 											  e -> buildPartData((JsonArray)e.getValue()) ));
				partInfoReadable = PartMapper.map.keySet().stream().map(key -> {
												JsonArray masteryArray = (JsonArray)masteryMap.get(key);
												if(masteryArray == null){
													return new PartDataTuple(key, PartDataDTO.builder().partName(key).ukInfo(new ArrayList<>()).build());
												}
												return new PartDataTuple(key, buildPartData(masteryArray, key));
											})
											.collect(Collectors.toMap(PartDataTuple::getKey, PartDataTuple::getPartData));

				masteryMap.entrySet().stream().forEach(entry -> {
					partInfo.setPartData(entry.getKey(), buildPartData((JsonArray)entry.getValue(), entry.getKey()));
				});
			}
			catch(Exception e){e.printStackTrace(); log.error("uk mastery parse error. {}. {}. {}",userId, probSetId, minitestMastery);}			
		}

		//Get LRS Record
		List<StatementDTO> statementList = null;
		try{
			statementList = lrsAPIManager.getStatementList(GetStatementInfoDTO.builder()
														  .userIdList(Arrays.asList(userId))
														  .actionTypeList(Arrays.asList("submit"))
														  .sourceTypeList(Arrays.asList("mini_test_question"))
														  .build());
		}
		catch(Exception e){e.printStackTrace(); return null;}
		statementList = filterPureLrsStatement(statementList);
		statementList = statementList.stream().parallel().filter(statement -> {
											try {if(JsonParser.parseString(statement.getExtension()).getAsJsonObject().get("diagProbSetId").getAsString().equals(probSetId)) return true;}
											catch(Exception e) {log.warn("extension parse error");return false;}
											return false;
										})
										.collect(Collectors.toList()); //filter by problem id
		if(statementList.size() == 0) return null;
		statementList = filterPureLrsStatement(statementList);

		//Problem info build
 		List<List<String>> problemInfoTotal = createProblemInfo(statementList, userId);

		List<List<String>> lowInfo = problemInfoTotal.stream().filter(info -> info.get(info.size()-1).equals("하")).collect(Collectors.toList());
		List<List<String>> midInfo = problemInfoTotal.stream().filter(info -> info.get(info.size()-1).equals("중")).collect(Collectors.toList());
		List<List<String>> highInfo = problemInfoTotal.stream().filter(info -> info.get(info.size()-1).equals("상")).collect(Collectors.toList());

		//Create stats
		Map<String, Object> problemCorrectInfo = new HashMap<>();
		Integer lowCorr = lowInfo.stream().mapToInt(info -> info.get(2).equals("true") ? 1 : 0).reduce(0, Integer::sum);
		Integer midCorr = midInfo.stream().mapToInt(info -> info.get(2).equals("true") ? 1 : 0).reduce(0, Integer::sum);
		Integer highCorr = highInfo.stream().mapToInt(info -> info.get(2).equals("true") ? 1 : 0).reduce(0, Integer::sum);
		

		//Set values
		problemCorrectInfo.put("allProb", problemInfoTotal.size());
		problemCorrectInfo.put("allCorr", lowCorr + midCorr + highCorr);
		problemCorrectInfo.put("low", lowCorr + "/" + lowInfo.size());
		problemCorrectInfo.put("mid", midCorr + "/" + midInfo.size());
		problemCorrectInfo.put("high", highCorr + "/" + highInfo.size());


		//Try alarm get for lrs statements
		int alarmcnt = statementList.stream().parallel()
								.mapToInt(statement -> {
									JsonObject extension = null;
									try{extension = JsonParser.parseString(statement.getExtension()).getAsJsonObject();}
									catch(Exception e){log.warn("extension can't be parsed. {}", statement.getExtension()); return 0;}

									Integer isGuessInt = null;
									try{isGuessInt = extension.get("guessAlarm").getAsInt();}
									catch(Exception e){}

									Boolean isGuessBool = null;
									try{isGuessBool = extension.get("guessAlarm").getAsBoolean();}
									catch(Exception e){}

									//If both value is null => warn and skip
									if(isGuessBool == null && isGuessInt == null){
										log.warn("guessAlarm invalid {}", extension.toString());
									}
									
									if(isGuessInt != null && isGuessInt > 0) return 1;
									if(isGuessBool != null && isGuessBool) return 1;

									return 0;
								})
								.reduce(0, Integer::sum);



		//TEMP comment template
		String commentTemplate = String.format("미니진단을 통해 분석된 지식점수에요.\n진단자 평균대비 %s 점수를 받으셨네요.\n이제 상세 분야별로 나의 지식 점수를 확인해보세요.\n우선적으로 학습해야하는 분야를 알 수 있습니다.",
											   report.getAvgUkMastery().intValue() > COMMENT_HIGH_LOW_SPLIT_THRESHOLD_SCORE ? "높은" : "낮은");

		
		MiniTestRecordDTO result =  MiniTestRecordDTO.builder()
								 .userName(report.getUser() != null ? report.getUser().getNickname() : null)
								 .totalScore(report.getAvgUkMastery().intValue())
								 .totalPercentage(sndCalculator.calculateForMiniTest((double)report.getAvgUkMastery().intValue()))
								 .partInfo(partInfo)
								 .partInfoReadable(partInfoReadable)
								 .problemLowLevelInfo(lowInfo)
								 .problemMiddleLevelInfo(midInfo)
								 .problemHighLevelInfo(highInfo)
								 .problemCorrectInfo(problemCorrectInfo)
								 .totalComment(commentTemplate)
								 .alarm(alarmcnt >= PICK_ALARM_CNT_THRESHOLD)
								 .build();

		return result;
	}


	public boolean checkMinireportExist(String userId, String probSetId){
		Optional<MinitestReport> queryResult = reportRepo.getReport(probSetId, userId);
		return queryResult.isPresent();
	}
	
}
