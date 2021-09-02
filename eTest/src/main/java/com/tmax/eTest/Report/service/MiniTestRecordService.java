package com.tmax.eTest.Report.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import com.tmax.eTest.Contents.repository.ProblemRepository;
import com.tmax.eTest.LRS.util.LRSAPIManager;
import com.tmax.eTest.Report.dto.MiniTestRecordDTO;
import com.tmax.eTest.Report.util.SNDCalculator;
// import com.tmax.eTest.Report.util.RuleBaseScoreCalculator;
// import com.tmax.eTest.Report.util.DiagnosisComment;
// import com.tmax.eTest.Report.util.StateAndProbProcess;
// import com.tmax.eTest.Report.util.TritonAPIManager;
// import com.tmax.eTest.Report.util.UKScoreCalculator;
// import com.tmax.eTest.Common.repository.report.DiagnosisReportRepo;
// import com.tmax.eTest.Test.repository.UserKnowledgeRepository;

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
													return Stream.of( Arrays.asList(uk.getUkName(), data.get(2), uk.getUkDescription(), "https://www.google.com") );
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
		AtomicInteger probIndex = new AtomicInteger(0);
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
				partInfoReadable = masteryMap.entrySet().stream()
									.collect(Collectors.toMap(e -> e.getKey(),
															  e -> buildPartData((JsonArray)e.getValue()) ));

				masteryMap.entrySet().stream().forEach(entry -> {
					partInfo.setPartData(entry.getKey(), buildPartData((JsonArray)entry.getValue()));
				});
			}
			catch(Exception e){e.printStackTrace(); log.error("uk mastery parse error. {}. {}. {}",userId, probSetId, minitestMastery);}			
		}

		//Get LRS Record
		List<StatementDTO> statementList = null;
		try{
			statementList = lrsAPIManager.getStatementList(GetStatementInfoDTO.builder()
														  .userIdList(Arrays.asList(userId))
														  .sourceTypeList(Arrays.asList("mini_test_question"))
														  .build());
		}
		catch(Exception e){e.printStackTrace(); return null;}
		if(statementList.size() == 0) return null;

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
		problemCorrectInfo.put("low", lowCorr + "/" + problemInfoTotal.size());
		problemCorrectInfo.put("mid", midCorr + "/" + problemInfoTotal.size());
		problemCorrectInfo.put("high", highCorr + "/" + problemInfoTotal.size());


		//Try alarm get for lrs statements
		int alarmcnt = statementList.stream().parallel()
								.mapToInt(statement -> {
									JsonObject extension = null;
									try{extension = JsonParser.parseString(statement.getExtension()).getAsJsonObject();}
									catch(Exception e){log.warn("extension can't be parsed. {}", statement.getExtension()); return 0;}

									int isGuess = 0;
									try{isGuess = extension.get("guessAlarm").getAsInt();}
									catch(Exception e){log.error("guessAlarm is not a valid int type"); return 0;}

									return (isGuess > 0) ? 1 : 0;
								})
								.reduce(0, Integer::sum);

		
		MiniTestRecordDTO result =  MiniTestRecordDTO.builder()
								 .userName(report.getUser().getNickname())
								 .totalScore(report.getAvgUkMastery().intValue())
								 .totalPercentage(sndCalculator.calculateForMiniTest((double)report.getAvgUkMastery().intValue()))
								 .partInfo(partInfo)
								 .partInfoReadable(partInfoReadable)
								 .problemLowLevelInfo(lowInfo)
								 .problemMiddleLevelInfo(midInfo)
								 .problemHighLevelInfo(highInfo)
								 .problemCorrectInfo(problemCorrectInfo)
								 .totalComment("")
								 .alarm(alarmcnt >= PICK_ALARM_CNT_THRESHOLD)
								 .build();



		return result;
	}
	
}
