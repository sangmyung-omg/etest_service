package com.tmax.eTest.TestStudio.controller.component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.tmax.eTest.Common.model.problem.DiagnosisProblem;
import com.tmax.eTest.Common.model.problem.Problem;
import com.tmax.eTest.Common.model.problem.ProblemChoice;
import com.tmax.eTest.Common.model.uk.ProblemUKRelation;
import com.tmax.eTest.Common.model.uk.UkMaster;
import com.tmax.eTest.TestStudio.dto.problems.base.BaseDiagCurriculumDTO;
import com.tmax.eTest.TestStudio.dto.problems.base.BaseDiagProblemSetDTO;
import com.tmax.eTest.TestStudio.dto.problems.base.BaseProbChoiceDTO;
import com.tmax.eTest.TestStudio.dto.problems.base.BaseProbUKRelDTO;
import com.tmax.eTest.TestStudio.dto.problems.base.BaseProblemDTO;
import com.tmax.eTest.TestStudio.dto.problems.in.PutDiagCurrStatusDTOIn;
import com.tmax.eTest.TestStudio.dto.problems.in.PutDiagProblemDTOIn;
import com.tmax.eTest.TestStudio.dto.problems.out.GetDiagProblemDTOOut;
import com.tmax.eTest.TestStudio.service.DiagCurriculumServiceETest;
import com.tmax.eTest.TestStudio.service.DiagProblemServiceETest;
import com.tmax.eTest.TestStudio.service.ProbChoiceServiceETest;
import com.tmax.eTest.TestStudio.service.ProbUKRelServiceETest;
import com.tmax.eTest.TestStudio.service.ProblemServiceETest;
import com.tmax.eTest.TestStudio.service.UKServiceETest;
import com.tmax.eTest.TestStudio.util.PathUtilEtest;

import lombok.RequiredArgsConstructor;

@Controller
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class DiagProblemApiComponentETest {
	
	private final ProblemServiceETest problemServiceETest;
	private final ProbChoiceServiceETest probChoiceServiceETest;
	private final ProbUKRelServiceETest probUKRelServiceETest;
	private final UKServiceETest ukServiceETest;
	private final DiagProblemServiceETest diagProblemServiceETest;
	private final DiagCurriculumServiceETest diagCurriculumServiceETest;
	
//	private final TestProblemApiComponentETest testProblemApiComponent;
	private final ImageFileServerApiComponentETest imageFileServerApiComponentETest;
	private final PathUtilEtest pathUtilEtest = new PathUtilEtest();
	
	/**
	 * 문제 조회
	 * 
	 */
	public GetDiagProblemDTOOut diagProblemsGetComponent(
//			String probIdStr
//			List<Long> probIdList
			List<String> strProbIdList
			) throws Exception{
		
		//
		
		GetDiagProblemDTOOut output = new GetDiagProblemDTOOut( new ArrayList<BaseDiagProblemSetDTO>() );

		// set : probId []
//		String[] strProbIdList = probIdStr.replace(" ","").split(",");
		
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
						imgJsonObjectNormToString,null
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
										PC.getUkIDOnly().toString(),
										PC.getChoiceScore().toString()
									)
								);
					}
				}
				
//
				List<ProblemUKRelation> problemUKRelations = probUKRelServiceETest.findAllWUKByProbId(probId);
		
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
			Long idx = -1L;
			if(request.getDiagProblems() != null)
			for(BaseDiagProblemSetDTO requestInfo__ : request.getDiagProblems()) {
				idx++;
				Long LongProbId = Long.parseLong(requestInfo__.getProblem().getProbID());
//				if( "출제".equals(requestInfo__.getDiagCurriculum().getStatus()) || "보류".equals(requestInfo__.getDiagCurriculum().getStatus())) {
//					requestInfo__.getProblem().setValidatorID("T");
//				}
				//problem table
				problemServiceETest.problemUpdate( request.getUserID() ,requestInfo__.getProblem());
				//
//				probChoiceServiceETest.probChoiceUpdate(request.getUserID(), requestInfo__.getProbChoices(), LongProbId);
				if(requestInfo__.getProbChoices()!=null) {
					if(!requestInfo__.getProbChoices().isEmpty()) {
						probChoiceServiceETest.probChoiceDeleteAllByProbId(LongProbId);
						for(BaseProbChoiceDTO PC : requestInfo__.getProbChoices()) {
							ProblemChoice problemChoice = new ProblemChoice();
//							Problem problemTemp = new Problem();
//							problemTemp.setProbID(problem.getProbID());
							problemChoice.setProbID(problemServiceETest.findOne(LongProbId));
//							problemChoice.setProbID(problemTemp);
							problemChoice.setChoiceNum( Long.parseLong( PC.getChoiceNum() ) );
							UkMaster ukMasterTemp = new UkMaster();
							ukMasterTemp.setUkId(Integer.parseInt( PC.getUkID() ) );
//							problemChoice.setUkId(ukServiceETest.findOneByUKId( Long.parseLong(PC.getUkID()) ));
							problemChoice.setUkId(ukMasterTemp);
							if(PC.getChoiceScore()!=null) {
								problemChoice.setChoiceScore( Integer.parseInt( PC.getChoiceScore() ) );
							}
							probChoiceServiceETest.probChoiceCreate(problemChoice);
						}
					}
				}
				//
				if(requestInfo__.getProbUKRels()!=null) {
					if(!requestInfo__.getProbUKRels().isEmpty()) {
						probUKRelServiceETest.probUKRelDeleteAllByProbId(LongProbId);
						for(BaseProbUKRelDTO PUR : requestInfo__.getProbUKRels()) {
							ProblemUKRelation problemUKRelation = new ProblemUKRelation();
							problemUKRelation.setProbID( problemServiceETest.findOne(LongProbId) );
							UkMaster ukMasterTemp = new UkMaster();
							ukMasterTemp.setUkId(Integer.parseInt( PUR.getUkID() ) );
//							problemUKRelation.setUkId( ukServiceETest.findOneByUKId( Long.parseLong(PUR.getUkID()) ) );
							problemUKRelation.setUkId(ukMasterTemp);							

							probUKRelServiceETest.probUKRelCreate(problemUKRelation);
						}
					}
				}
				//
				if(requestInfo__.getDiagCurriculum()!=null)
				diagCurriculumServiceETest.problemUpdate( request.getUserID() ,requestInfo__.getDiagCurriculum());
				
				// problem image
				if(requestInfo__.getProblem().getImgListIn()!=null) {
					
					File folder = new File( pathUtilEtest.getDirPath()
											+ File.separator + requestInfo__.getProblem().getProbID() );
					
					if( folder.exists() ){
						List<String> willDelImgData = new ArrayList<String>();
						for( String imgSrc__ : folder.list() ) {
							willDelImgData.add(imgSrc__);
						}
						for(String src: requestInfo__.getProblem().getImgListIn()) {
							willDelImgData.remove(src);
						}
						imageFileServerApiComponentETest.deleteImgSrcsOfProbIDServiceComponent(LongProbId, willDelImgData );
					}
					Boolean isSuccess = imageFileServerApiComponentETest
							.assignImgFileListServiceComponent(request.getUserID(), LongProbId, requestInfo__.getProblem().getImgListIn());
				
				}
				
			}			
			
			imageFileServerApiComponentETest.deleteImgTempFolerOfUserIDServiceComponent(request.getUserID());
			
			return "success";
		}catch(Exception e) {
			 e.printStackTrace(); 
			 return "fail";		
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
				for( DiagnosisProblem diagnosisProblem 
						: diagProblemServiceETest.findByIdOrderSorted( Long.parseLong( request.getCurriculumID() ) )
					){
					
					problemServiceETest.problemValidate(request.getUserID(), diagnosisProblem.getProbId().toString());
				}
			}
	
			return "success";
		}catch(Exception e) {
			 e.printStackTrace(); 
			 return "fail";		
		}
	}
	
	
}
