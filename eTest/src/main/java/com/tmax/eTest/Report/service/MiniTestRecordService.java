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
import com.tmax.eTest.Common.repository.user.UserMasterRepo;
import com.tmax.eTest.Common.model.uk.UkMaster;
import com.tmax.eTest.Common.model.user.UserMaster;

import java.util.Optional;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.Set;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;

import java.sql.Timestamp;
import java.text.ParseException;
import java.lang.reflect.Type;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
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
	@Autowired private ProblemRepository problemRepo;
	@Autowired private SNDCalculator sndCalculator;

	@Autowired private MinitestReportRepo reportRepo;

	@Autowired private UkMasterRepo ukMasterRepo;

	@Autowired private UserMasterRepo userMasterRepo;


	private static final int PICK_ALARM_CNT_THRESHOLD = 3;
	private static final int COMMENT_HIGH_LOW_SPLIT_THRESHOLD_SCORE = 60;

	private static final int COMMENT_HIGH_MID_SPLIT_THRESHOLD_SCORE = 80;
	private static final int COMMENT_MID_LOW_SPLIT_THRESHOLD_SCORE = 60;

	private static final int UK_INFO_MASTERY_INDEX = 1;

	private static final int UK_INFO_SLICE_LENGTH = 5;

	private PartDataDTO buildPartData(JsonArray mastery, String partname){
		PartDataDTO output = buildPartData(mastery);
		output.setPartName(partname);
		output.setPercentage( (long)sndCalculator.calculatePercentage(PartMapper.sndCalcTypeMap.get(partname), output.getScore().intValue() ) );
		return output;
	}

	private PartDataDTO buildPartData(JsonArray mastery){
		//Parse raw data
		Type typeLLS = new TypeToken< List<List<String>> >(){}.getType();
		List<List<String>> ukDataList = null;
		try{ukDataList =  new Gson().fromJson(mastery, typeLLS);}
		catch(JsonSyntaxException e){log.error("mastery parse error {}", mastery.toString()); return null;}

		//Filter invalid entries
		ukDataList = ukDataList.stream().filter(data -> data.size() == 3).collect(Collectors.toList());

		//Get all ukIdList
		Set<Integer> ukIdSet = ukDataList.stream().parallel()
										 .flatMap(data -> {
											 try{ return Stream.of(Integer.parseInt(data.get(0)));}
											 catch(NumberFormatException e){log.error("uk id parse error {}", data.toString()); return Stream.empty();}
										 }).collect(Collectors.toSet());
		Map<Integer, UkMaster> ukDetailMap = ukMasterRepo.findAllById(ukIdSet).stream()
														 .collect(Collectors.toMap(UkMaster::getUkId, uk -> uk));

		//For each uk --> try parse score + build ukInfo
		double totalScore = ukDataList.stream()
				.flatMap(data -> {
					//3rd element is score --> try float conversion
					try{return Stream.of( Double.parseDouble(data.get(2)) );}
					catch(NullPointerException e){log.error("score is null {}", data); return Stream.empty();}
					catch(NumberFormatException e){log.error("score parse error {}", data); return Stream.empty();}
				}).reduce(0.0, Double::sum);
		
		List<List<String>> ukInfo = ukDataList.stream().parallel()
											  .flatMap(data -> {
													Integer ukId = null;
													try{ukId = Integer.parseInt(data.get(0));}
													catch(NumberFormatException e){log.warn("ukid parse error. {}", data.toString());return Stream.empty();}

													UkMaster uk = ukDetailMap.get(ukId);
													if(uk == null)
														return Stream.empty();
													
													//build info (name, score, desc, link)	
													return Stream.of( Arrays.asList(uk.getUkName(), data.get(2), uk.getUkDescription(), uk.getExternalLink() ) );
											  }).collect(Collectors.toList());
		Collections.sort(ukInfo, (a,b) -> Double.compare( Double.parseDouble(a.get(UK_INFO_MASTERY_INDEX)), Double.parseDouble(b.get(UK_INFO_MASTERY_INDEX)))); //sort by mastery
		ukInfo = ukInfo.subList(0, Math.min(UK_INFO_SLICE_LENGTH, ukInfo.size())); //slice list to desired length

		long score = (long)Math.floor(totalScore / ukDataList.size());

		return PartDataDTO.builder()
							.score(score).ukInfo(ukInfo)
							// .percentage((long)sndCalculator.calculatePercentage(PartMapper.sndCalcTypeMap.get(), )))
							.build();
	}

	private List<List<String>> createProblemInfo(List<StatementDTO> statementList, String userId){
		//Order lrs by time
		try{
			Collections.sort(statementList, (a,b)->{
				//Default => user statement_date
				Timestamp timestampA = a.getStatementDate();
				Timestamp timestampB = b.getStatementDate();

				//Fall back to timestamp strings
				if(timestampA == null || timestampB == null){
					String stringTStampA = a.getTimestamp();
					String stringTStampB = b.getTimestamp();

					//if both seems like Zoned time data
					if(stringTStampA.endsWith("Z") && stringTStampB.endsWith("Z")){
						return ZonedDateTime.parse(stringTStampA).compareTo(ZonedDateTime.parse(stringTStampB));
					}

					
					//Prune "Z" at the end if exists
					log.info("presuming local timestamp for both cases, {} {}", stringTStampA, stringTStampB);
					stringTStampA = stringTStampA.replace("Z", "");
					stringTStampB = stringTStampB.replace("Z", "");

					return LocalDateTime.parse(stringTStampA).compareTo(LocalDateTime.parse(stringTStampB));
				}


				//best case comparision
				return timestampA.compareTo(timestampB);
			});
		}
		catch(DateTimeParseException e){log.error("Timestamp sort failed due to parse error. will use unsorted lrs list. {}", e.getMessage());}

		//Find all prob info in lrs
		Set<Integer> probIdSet = statementList.stream().parallel()
									.map(StatementDTO::getSourceId)
									.flatMap(strId -> {
										try {return Stream.of(Integer.parseInt(strId));}
										catch(NumberFormatException e){return Stream.empty();} 
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
														catch(NumberFormatException e){return Stream.empty();}

														//get question test from question column and diff data
														Problem prob = probMap.get(probId);
														if(prob == null){
															log.warn("Prob {} not found skipping info build for this Q", probId);
															return Stream.empty();
														}

														//Get data with type "QUESTION_TEXT"
														JsonArray questionDataList = null; 
														try {questionDataList = JsonParser.parseString(prob.getQuestion()).getAsJsonArray();}
														catch(JsonParseException e){log.error("question data json invalid for {}", probId); return Stream.empty();}

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
																					   prob.getDifficulty(),
																					   prob.getIntention()
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
			catch(DateTimeParseException e){log.warn("Timestamp error in LRS statement {}", srcIdStatementMap.get(srcId).getTimestamp());}
			try{currTime = ZonedDateTime.parse(statement.getTimestamp());}
			catch(DateTimeParseException e){log.warn("Timestamp error in LRS statement {}", statement.getTimestamp());}

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
			catch(JsonParseException e){log.error("uk mastery parse error. {}. {}. {}",userId, probSetId, minitestMastery);}			
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
		catch(ParseException e){log.warn("lrs api manager failed."); return null;}
		statementList = statementList.stream().parallel().filter(statement -> {
											try {if(JsonParser.parseString(statement.getExtension()).getAsJsonObject().get("diagProbSetId").getAsString().equals(probSetId)) return true;}
											catch(JsonParseException e) {log.warn("extension parse error");return false;}
											return false;
										})
										.collect(Collectors.toList()); //filter by problem id
		if(statementList.size() == 0) return null;
		statementList = filterPureLrsStatement(statementList);

		//Problem info build
 		List<List<String>> problemInfoTotal = createProblemInfo(statementList, userId);

		List<List<String>> lowInfo = problemInfoTotal.stream().filter(info -> info.get(info.size()-2).equals("하")).collect(Collectors.toList());
		List<List<String>> midInfo = problemInfoTotal.stream().filter(info -> info.get(info.size()-2).equals("중")).collect(Collectors.toList());
		List<List<String>> highInfo = problemInfoTotal.stream().filter(info -> info.get(info.size()-2).equals("상")).collect(Collectors.toList());

		//Create stats
		Map<String, Object> problemCorrectInfo = new HashMap<>();
		Integer lowCorr = lowInfo.stream().mapToInt(info -> info.get(2).equals("true") ? 1 : 0).reduce(0, Integer::sum);
		Integer midCorr = midInfo.stream().mapToInt(info -> info.get(2).equals("true") ? 1 : 0).reduce(0, Integer::sum);
		Integer highCorr = highInfo.stream().mapToInt(info -> info.get(2).equals("true") ? 1 : 0).reduce(0, Integer::sum);
		

		//Set values
		problemCorrectInfo.put("allProb", problemInfoTotal.size());
		problemCorrectInfo.put("allCorr", lowCorr + midCorr + highCorr);
		problemCorrectInfo.put("low", lowCorr + "/" + lowInfo.size());
		problemCorrectInfo.put("middle", midCorr + "/" + midInfo.size());
		problemCorrectInfo.put("high", highCorr + "/" + highInfo.size());


		//Try alarm get for lrs statements
		int alarmcnt = statementList.stream().parallel()
								.mapToInt(statement -> {
									JsonObject extension = null;
									try{extension = JsonParser.parseString(statement.getExtension()).getAsJsonObject();}
									catch(JsonParseException e){log.warn("extension can't be parsed. {}", statement.getExtension()); return 0;}

									Integer isGuessInt = null;
									try{isGuessInt = extension.get("guessAlarm").getAsInt();}
									catch(ClassCastException e){log.debug("extension cast failed.");}
									catch(IllegalStateException e){log.debug("extension state invalid.");}
									catch(NumberFormatException e){log.debug("extension cast failed.");}
									catch(NullPointerException e){log.debug("extension field is null");}

									Boolean isGuessBool = null;
									try{isGuessBool = extension.get("guessAlarm").getAsBoolean();}
									catch(ClassCastException e){log.debug("extension cast failed.");}
									catch(IllegalStateException e){log.debug("extension state invalid.");}
									catch(NullPointerException e){log.debug("extension field is null");}

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
		// String commentTemplate = String.format("미니진단을 통해 분석된 지식점수에요.\n진단자 평균대비 %s 점수를 받으셨네요.\n이제 상세 분야별로 나의 지식 점수를 확인해보세요.\n우선적으로 학습해야하는 분야를 알 수 있습니다.",
		// 									   report.getAvgUkMastery().intValue() > COMMENT_HIGH_LOW_SPLIT_THRESHOLD_SCORE ? "높은" : "낮은");

		//New comment template input 2021-10-11 Jonghyun-seong
		String comment = "";
		if(report.getAvgUkMastery().intValue() < COMMENT_MID_LOW_SPLIT_THRESHOLD_SCORE){ // under 60 (~59)
			comment = "주식투자를 시작한지 얼마 되지 않거나, 아직은 주식투자에 익숙하지 않으신가요? 시장과 산업동향, 주식 투자의 기본 등 꼼꼼하고 꾸준하게 공부하는 투자자가 결국에는 오래 투자할 수 있습니다. 투자와 친해지기 위한 첫걸음을 떼세요. 한 걸음 한 걸음 내딛는다면 투자고수로의 길이 열릴 수 있습니다.";
		}
		else if(report.getAvgUkMastery().intValue() < COMMENT_HIGH_MID_SPLIT_THRESHOLD_SCORE){ // under 80 (60~79)
			comment = "당신은 투자의 기본적인 지식은 갖춘 투자자 입니다. 투자에 관심이 커지면서 주변으로부터 접하는 투자정보가 많아지고 있지는 않으신가요? 이럴때일수록 주식을 깊이 있게 공부하고 자신만의 투자원칙이 필요합니다. 지식과 경험을 쌓아간다면 주변의 소문과 주가 등락에도 흔들리지 않고 나아갈 수 있는 힘을 키울 수 있을 겁니다.";
		}
		else { //eq or more than 80
			comment = "당신은 투자 기본부터 상품을 선택하고 관리하는 방법까지 투자에 필요한 전반적인 지식은 갖춘 투자자 입니다. 자신만의 투자방법과 원칙을 갖고 있을 가능성이 큽니다. 하지만 방심은 금물! 투자지식 뿐만 아니라 급변하는 사회, 경제, 정치 등 다양한 분야에 대한 관심을 이어간다면 지금처럼 초보 투자자의 귀감이 되는 주식고수의 자리를 유지할 수 있을 겁니다.";
		}

		
		UserMaster userData = report.getUser();
		if(userData == null){ //fallback get from user master
			log.warn("user data not retrieved from minireport join call. Fetching directly from user_master. {}", report.toString());
			Optional<UserMaster> data = userMasterRepo.findById(report.getUserUuid());
			userData = data.isPresent() ? data.get() : null;

			//If still null
			if(userData == null){
				log.warn("user [{}] not found in user_master. using null as nickname", report.getUserUuid());
			}
		}
		
		MiniTestRecordDTO result =  MiniTestRecordDTO.builder()
								 .userName(userData.getNickname())
								 .totalScore(report.getAvgUkMastery().intValue())
								 .totalPercentage(sndCalculator.calculatePercentage(SNDCalculator.Type.MINI_TOTAL, report.getAvgUkMastery().intValue()))
								 .partInfo(partInfo)
								 .partInfoReadable(partInfoReadable)
								 .problemLowLevelInfo(lowInfo)
								 .problemMiddleLevelInfo(midInfo)
								 .problemHighLevelInfo(highInfo)
								 .problemCorrectInfo(problemCorrectInfo)
								 .totalComment(comment)
								 .alarm(alarmcnt >= PICK_ALARM_CNT_THRESHOLD)
								 .build();

		return result;
	}


	public boolean checkMinireportExist(String userId, String probSetId){
		Optional<MinitestReport> queryResult = reportRepo.getReport(probSetId, userId);
		return queryResult.isPresent();
	}
	
}
