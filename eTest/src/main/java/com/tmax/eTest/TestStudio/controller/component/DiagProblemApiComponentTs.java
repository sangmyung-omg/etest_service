package com.tmax.eTest.TestStudio.controller.component;

import java.io.Console;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.tmax.eTest.Common.model.problem.DiagnosisProblem;
import com.tmax.eTest.Common.model.problem.Problem;
import com.tmax.eTest.Common.model.problem.ProblemChoice;
import com.tmax.eTest.Common.model.uk.ProblemUKRelation;
import com.tmax.eTest.Common.model.uk.UkMaster;
import com.tmax.eTest.TestStudio.controller.TestProblemControllerTs;
import com.tmax.eTest.TestStudio.dto.problems.base.BaseDiagCurriculumDTO;
import com.tmax.eTest.TestStudio.dto.problems.base.BaseDiagProblemSetDTO;
import com.tmax.eTest.TestStudio.dto.problems.base.BaseProbChoiceDTO;
import com.tmax.eTest.TestStudio.dto.problems.base.BaseProbUKRelDTO;
import com.tmax.eTest.TestStudio.dto.problems.base.BaseProblemDTO;
import com.tmax.eTest.TestStudio.dto.problems.base.BaseProblemSetDTO;
import com.tmax.eTest.TestStudio.dto.problems.in.PutDiagCurrStatusDTOIn;
import com.tmax.eTest.TestStudio.dto.problems.in.PutDiagProblemDTOIn;
import com.tmax.eTest.TestStudio.dto.problems.out.GetDiagProblemDTOOut;
import com.tmax.eTest.TestStudio.service.DiagCurriculumServiceTs;
import com.tmax.eTest.TestStudio.service.DiagProblemServiceTs;
import com.tmax.eTest.TestStudio.service.ProbChoiceServiceTs;
import com.tmax.eTest.TestStudio.service.ProbUKRelServiceTs;
import com.tmax.eTest.TestStudio.service.ProblemServiceTs;
import com.tmax.eTest.TestStudio.service.UKServiceTs;
import com.tmax.eTest.TestStudio.util.PathUtilTs;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional(rollbackFor = {Exception.class})
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Slf4j
public class DiagProblemApiComponentTs {
	
	private final ProblemServiceTs problemServiceETest;
	private final ProbChoiceServiceTs probChoiceServiceETest;
	private final ProbUKRelServiceTs probUKRelServiceETest;
	private final UKServiceTs ukServiceETest;
	private final DiagProblemServiceTs diagProblemServiceETest;
	private final DiagCurriculumServiceTs diagCurriculumServiceETest;
	
//	private final TestProblemApiComponentETest testProblemApiComponent;
	private final	ProblemApiComponentTs problemApiComponentTs;
	private final ImageFileServerApiComponentTs imageFileServerApiComponentETest;
	private final PathUtilTs pathUtilEtest;
	
	/**
	 * 문제 조회
	 * 
	 */
	public GetDiagProblemDTOOut diagProblemsGetComponent(
			String probIdStr
//			List<Long> probIdList
//			List<String> strProbIdList
			) throws Exception{
		
		//
		
		GetDiagProblemDTOOut output = new GetDiagProblemDTOOut( new ArrayList<BaseDiagProblemSetDTO>() );

		// set : probId []
		String[] strProbIdList = probIdStr.replace(" ","").split(",");
		
			for(String strProbId : strProbIdList) {
				if(strProbId==null||strProbId=="") {
					output.getDiagProblems().add(null);
					continue;
				}
				Long probId = Long.parseLong(strProbId);
				
//			for(Long probId : probIdList) {
//				if(probId==null) {
//					output.getDiagProblems().add(null);
//					continue;
//				}
				BaseDiagProblemSetDTO outputBase = new BaseDiagProblemSetDTO();
				
				Problem findProblem = problemServiceETest.findOneSet(probId);
				
				List<String> ImgJsonToStrList = imageFileServerApiComponentETest.getImgJsonToStrListByProbIDServiceComponent(probId);
				String imgJsonObjectNormToString = ImgJsonToStrList.get(0);
//				String imgJsonObjectDarkToString = ImgJsonToStrList.get(1);			
				 
				BaseProblemDTO collect = new BaseProblemDTO(
						findProblem.getProbID().toString(), findProblem.getAnswerType(),
						findProblem.getQuestion(), findProblem.getSolution(),
						findProblem.getDifficulty(), findProblem.getCategory(),
						findProblem.getImgSrc(),
						findProblem.getTimeReco()==null? null: findProblem.getTimeReco().toString(),
						findProblem.getCreatorId(), findProblem.getCreateDate(),
						findProblem.getValiatorID(), findProblem.getValiateDate(),
						findProblem.getEditorID(), findProblem.getEditDate(),
						findProblem.getSource(), findProblem.getIntention(),
						findProblem.getQuestionInitial(),findProblem.getSolutionInitial(),
						imgJsonObjectNormToString,null,
						null
//						,null,null
						);
				
				outputBase.setProblem(collect); 
				
//				System.out.println( findProblem.getProblemChoices().get(0).getChoiceNum() );
//				List<ProblemChoice> problemChoiceList = probChoiceServiceETest.findAllByProbId(probId);
				List<ProblemChoice> problemChoices = findProblem.getProblemChoices();
				if(problemChoices != null) {
					outputBase.setProbChoices(new ArrayList<BaseProbChoiceDTO>());
					for( ProblemChoice PC : problemChoices) {
						outputBase.getProbChoices().add(new BaseProbChoiceDTO(
//										PC.getProbIDOnly().toString(), //probId
										null,
										String.valueOf( PC.getChoiceNum() ),
										PC.getUkIDOnly()==null? null: PC.getUkIDOnly().toString(),
										PC.getChoiceScore()==null? null: PC.getChoiceScore().toString()
									)
								);
					}
				}
				
//
				List<ProblemUKRelation> problemUKRelations = findProblem.getProblemUKReleations(); 
//				= probUKRelServiceETest.findAllWUKByProbId(probId);
		
				if(problemUKRelations != null) {
					outputBase.setProbUKRels(new ArrayList<BaseProbUKRelDTO>());
					for( ProblemUKRelation UKR : problemUKRelations) {
						outputBase.getProbUKRels().add(new BaseProbUKRelDTO(
//										probId.toString(),
										null,
										UKR.getUkId().getUkId().toString()
									)
								);
					}
				}	
				
//				String status = diagCurriculumServiceETest.findOne( diagProblemServiceETest.findOne(probId).getCurriculumId().longValue() ).getStatus();
				if(findProblem.getDiagnosisInfo() ==null) {
					throw new Exception("DiagnosisInfo() ==null");
				}
				String status = findProblem.getDiagnosisInfo().getCurriculum().getStatus();
				BaseDiagCurriculumDTO baseDiagCurriculumDTO = new BaseDiagCurriculumDTO();
				baseDiagCurriculumDTO.setStatus(status);
				outputBase.setDiagCurriculum(baseDiagCurriculumDTO);
				
				output.getDiagProblems().add(outputBase);
			}
			return output;
		
		//
		
		
	}
	
	
	/**
	 * 문제 업데이트 problem Update component
	 * request form, isDiagProb(Boolean) 받아 문제 update, ProbUpdateResponse반환
	 */
	public String updateProblemcomponent(PutDiagProblemDTOIn request) throws Exception{
		try {
						
			// 문제 n개 업데이트

			if(request.getDiagProblems() != null)
			for(BaseDiagProblemSetDTO requestInfo__ : request.getDiagProblems()) {

				BaseProblemSetDTO baseProblemSetDTO = new BaseProblemSetDTO();
				baseProblemSetDTO.setProblem( requestInfo__.getProblem() );
				baseProblemSetDTO.setProbChoices( requestInfo__.getProbChoices() );
				baseProblemSetDTO.setProbUKRels( requestInfo__.getProbUKRels() );
				
				problemApiComponentTs.updateBasicProblemcomponent(baseProblemSetDTO, request.getUserID());
				//
				if(requestInfo__.getDiagCurriculum()!=null)
				diagCurriculumServiceETest.problemUpdate( request.getUserID() ,requestInfo__.getDiagCurriculum());
			
			}			
			
			imageFileServerApiComponentETest.deleteImgTempFolerOfUserIDServiceComponent(request.getUserID());
			
			return "success";
		}catch(Exception e) {
			 throw e;	
		}
	}
	
	
	/**
	 * curr status 변경
	 * 
	 */
	public String updateDiagCurrStatus(PutDiagCurrStatusDTOIn request) throws Exception{
		try {
						
			// 문제 n개 업데이트
			Long idx = -1L;
			if(request.getUserID() == null || request.getCurriculumID() ==null) return null;
			
			if( "ok".equals(diagCurriculumServiceETest.currStatusChange( request.getCurriculumID() ) ) ){
				if( null != diagProblemServiceETest.findByIdOrderSorted( Long.parseLong( request.getCurriculumID() ) ) ){
					for( DiagnosisProblem diagnosisProblem 
							: diagProblemServiceETest.findByIdOrderSorted( Long.parseLong( request.getCurriculumID() ) )
						){
						
						problemServiceETest.problemValidate(request.getUserID(), diagnosisProblem.getProbId().toString());
					}
				}
			}
	
			return "success";
		}catch(Exception e) {
			 e.printStackTrace(); 
			 return "fail";		
		}
	}
	
	
}
