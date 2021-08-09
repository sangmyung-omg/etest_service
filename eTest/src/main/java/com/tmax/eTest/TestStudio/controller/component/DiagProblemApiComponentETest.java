package com.tmax.eTest.TestStudio.controller.component;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.tmax.eTest.Common.model.problem.Problem;
import com.tmax.eTest.TestStudio.dto.problems.base.BaseDiagCurriculumDTO;
import com.tmax.eTest.TestStudio.dto.problems.base.BaseDiagCurriculumandProblemDTO;
import com.tmax.eTest.TestStudio.dto.problems.base.BaseProblemDTO;
import com.tmax.eTest.TestStudio.dto.problems.in.PutDiagProblemDTOIn;
import com.tmax.eTest.TestStudio.dto.problems.out.GetDiagProblemDTOOut;
import com.tmax.eTest.TestStudio.service.DiagCurriculumServiceETest;
import com.tmax.eTest.TestStudio.service.DiagProblemServiceETest;
import com.tmax.eTest.TestStudio.service.ProblemServiceETest;
import com.tmax.eTest.TestStudio.util.PathUtilEtest;

import lombok.RequiredArgsConstructor;

@Controller
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class DiagProblemApiComponentETest {
	
	private final ProblemServiceETest problemServiceETest;
	private final DiagProblemServiceETest diagProblemServiceETest;
	private final DiagCurriculumServiceETest diagCurriculumServiceETest;
	
	private final TestProblemApiComponentETest testProblemApiComponent;
	private final ImageFileServerApiComponentETest imageFileServerApiComponentETest;
	private final PathUtilEtest pathUtilEtest = new PathUtilEtest();
	
	/**
	 * 문제 조회
	 * 
	 */
	public GetDiagProblemDTOOut diagProblemsGetComponent(String probIdStr) throws Exception{
		
		//
		
		GetDiagProblemDTOOut output = new GetDiagProblemDTOOut( new ArrayList<BaseDiagCurriculumandProblemDTO>() );
		BaseDiagCurriculumandProblemDTO outputBase = new BaseDiagCurriculumandProblemDTO();
		// set : probId []
		String[] strProbIdList = probIdStr.replace(" ","").split(",");
		
			for(String strProbId : strProbIdList) {
				if(strProbId==null||strProbId=="") {
					output.getDiagProblems().add(null);
					continue;
				}
				Long probId = Long.parseLong(strProbId);
				
				//id로 Problem 정보 검색
				Problem findProblem = problemServiceETest.findOne(probId);
				
				//id로 이미지 정보 검색 
				List<String> ImgJsonToStrList = imageFileServerApiComponentETest.getImgJsonToStrListByProbIDServiceComponent(probId);
				String imgJsonObjectNormToString = ImgJsonToStrList.get(0);
//				String imgJsonObjectDarkToString = ImgJsonToStrList.get(1);
				//
				
				 
				//엔터티 -> DTO 변환
				BaseProblemDTO collect = new BaseProblemDTO(
						findProblem.getProbID().toString(), findProblem.getAnswerType(),
						findProblem.getQuestion(), findProblem.getSolution(),
						findProblem.getDifficulty(), findProblem.getCategory(),
						findProblem.getImgSrc(), findProblem.getTimeReco(),
						findProblem.getCreatorId(), new Timestamp(findProblem.getCreateDate().getTime()),
						findProblem.getValiatorID(), new Timestamp(findProblem.getValiateDate().getTime()),
						findProblem.getEditorID(), new Timestamp(findProblem.getEditDate().getTime()),
						findProblem.getSource(), findProblem.getIntention(),
						findProblem.getQuestionInitial(),findProblem.getSolutionInitial(),
						imgJsonObjectNormToString,null,
						null,null
						);
				
				outputBase.setProblem(collect); 
				
//				String status = diagCurriculumServiceETest.findOne( diagProblemServiceETest.findOne(probId).getCurriculumId().longValue() ).getStatus();
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
			for(BaseDiagCurriculumandProblemDTO requestInfo__ : request.getDiagProblems()) {
				idx++;
				Long LongProbId = Long.parseLong(requestInfo__.getProblem().getProbID());
//				if( "출제".equals(requestInfo__.getDiagCurriculum().getStatus()) || "보류".equals(requestInfo__.getDiagCurriculum().getStatus())) {
//					requestInfo__.getProblem().setValidatorID("T");
//				}
				//problem table
				problemServiceETest.problemUpdate( request.getUserID() ,requestInfo__.getProblem());
				
				//
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
	
}
