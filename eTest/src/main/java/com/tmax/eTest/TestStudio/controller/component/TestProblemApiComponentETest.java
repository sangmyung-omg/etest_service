package com.tmax.eTest.TestStudio.controller.component;

import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.tmax.eTest.Common.model.problem.Problem;
import com.tmax.eTest.Common.model.problem.TestProblem;
import com.tmax.eTest.TestStudio.dto.problems.in.PostTestProblemDTOIn;
import com.tmax.eTest.TestStudio.dto.problems.in.PutDiagProblemDTOIn;
import com.tmax.eTest.TestStudio.dto.problems.in.PutTestProblemDTOIn;
import com.tmax.eTest.TestStudio.dto.problems.out.GetDiagProblemDTOOut;
import com.tmax.eTest.TestStudio.dto.problems.out.GetTestProblemDTOOut;
import com.tmax.eTest.TestStudio.dto.problems.base.BaseDiagCurriculumDTO;
import com.tmax.eTest.TestStudio.dto.problems.base.BaseProblemDTO;
import com.tmax.eTest.TestStudio.dto.problems.base.BaseTestProblemDTO;
import com.tmax.eTest.TestStudio.dto.problems.base.BaseTestProblemandProblemDTO;
import com.tmax.eTest.TestStudio.service.ProblemServiceETest;
import com.tmax.eTest.TestStudio.service.TestProblemServiceETest;
import com.tmax.eTest.TestStudio.util.PathUtilEtest;

import lombok.RequiredArgsConstructor;

@Controller
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class TestProblemApiComponentETest {
	
	private final ProblemServiceETest problemServiceETest;
	private final TestProblemServiceETest testProblemServiceETest;
	
	private final ImageFileServerApiComponentETest imageFileServerApiComponentETest;
	private final PathUtilEtest pathUtilEtest = new PathUtilEtest();
	
	/**
	 * 문제 생성 problemCreate component
	 * request form 받아 생성한 문제 id list 반환
	 */
	
	public List<List<Long>> CreateProblemComponet(PostTestProblemDTOIn request) throws Exception {
		
		try {
			
			List<List<Long>> result = new ArrayList<List<Long>>();
			
			List<Long> probIdList = new ArrayList<Long>();
	
//			Map< String, List<Long> > skipMap =  checkCreateProblemSkip(request);
//			List<Long> skipIdxList = skipMap.get("skipIdxList");
			
			List<Long> imgFailProbIdList = new ArrayList<Long>();
			Long idx =-1L;
			for(BaseTestProblemandProblemDTO requestInfo__ : request.getTestProblems()) {
				
				BaseProblemDTO requestInfo = requestInfo__.getProblem();
				idx++;
				Boolean imgSuccess = true;
				
				Problem problem = new Problem();
				Timestamp timestamp = new Timestamp(System.currentTimeMillis());
				
//				if(skipIdxList.contains(idx)) {
//					continue;
//				}
				
				problem.setProbID( Integer.valueOf( requestInfo.getProbID() )  );
				problem.setAnswerType(requestInfo.getAnswerType());
				problem.setQuestion(requestInfo.getQuestion());
				problem.setSolution(requestInfo.getSolution());
				problem.setDifficulty(requestInfo.getDifficulty());
				problem.setCategory(requestInfo.getCategory());
				problem.setImgSrc(requestInfo.getImgSrc());
				problem.setTimeReco(requestInfo.getTimeRecommendation());
//				problem.setCreatorId(requestInfo.getCreatorID());
//				problem.setCreateDate(requestInfo.getCreateDate());
//				problem.setValiatorID(requestInfo.getValidatorID());
//				problem.setValiateDate(requestInfo.getValidateDate());
//				problem.setEditorID(requestInfo.getEditorID());
//				problem.setEditDate(requestInfo.getEditDate());
				problem.setSource(requestInfo.getSource());
				problem.setIntention(requestInfo.getIntention());
				problem.setQuestionInitial(requestInfo.getQuestionInitial());
				problem.setSolutionInitial(requestInfo.getSolutionInitial());
						
				problem.setCreatorId(request.getUserID());
				problem.setCreateDate(timestamp);
				if( "출제".equals(requestInfo__.getTestProblem().getStatus()) || "보류".equals(requestInfo__.getTestProblem().getStatus())) {
					problem.setValiatorID(request.getUserID());
					problem.setValiateDate(timestamp);
				}
				
				
				/*
				 * problem table
				 */
				problemServiceETest.problemCreate(problem);
//				System.out.println(problem.getProbId());
				
				
				TestProblem testProblem = new TestProblem();
				testProblem.setProbID(problem.getProbID());
				testProblem.setPartID(Integer.parseInt( requestInfo__.getTestProblem().getPartID() ));
				testProblem.setSubject(requestInfo__.getTestProblem().getSubject());
				testProblem.setStatus(requestInfo__.getTestProblem().getStatus());
				testProblemServiceETest.testProblemCreate(testProblem);
				
				/*
				 * probImage table
				 */
				
				if(requestInfo.getImgListIn()!=null) {
					if(!requestInfo.getImgListIn().isEmpty()) {
							
						if(requestInfo.getImgListIn()!=null) {
							if(!requestInfo.getImgListIn().isEmpty()) {
								Problem problem__ = problemServiceETest.findOne(problem.getProbID().longValue());
								problem__.setImgSrc(pathUtilEtest.getDirPath() + File.separator + problem.getProbID());
							}
						}
						imageFileServerApiComponentETest.deleteImgSrcFileOfProbIDServiceComponent( problem.getProbID().longValue() );
						Boolean isSuccess = imageFileServerApiComponentETest
								.assignImgFileListServiceComponent(
																   request.getUserID(), 
																   problem.getProbID().longValue(),
																   requestInfo.getImgListIn()
																  );
						if(!isSuccess) imgSuccess=false;
					}
				}

				probIdList.add(problem.getProbID().longValue());
			
				if(!imgSuccess) {
					imgFailProbIdList.add(problem.getProbID().longValue());
				}
				
			}

			imageFileServerApiComponentETest.deleteImgTempFolerOfUserIDServiceComponent(request.getUserID());
			
			result.add(probIdList);
			result.add(null); //			result.add(skipIdxList);
			result.add(imgFailProbIdList);

			return result;
			
		}catch (Exception e) {
			throw e;
		}
	}
	
	
	
	/**
	 * prob 문제 조회 
	 * probId List 받아 문제 조회, GetTestProblemDTOOut 반환
	 */	
	public GetTestProblemDTOOut testProblemsGetComponent(String probIdStr) throws Exception {
		
			GetTestProblemDTOOut output = new GetTestProblemDTOOut( new ArrayList<BaseTestProblemandProblemDTO>() );
			BaseTestProblemandProblemDTO outputBase = new BaseTestProblemandProblemDTO();
			// set : probId []
			String[] strProbIdList = probIdStr.replace(" ","").split(",");
			
				for(String strProbId : strProbIdList) {
					if(strProbId==null||strProbId=="") {
						output.getTestProblems().add(null);
						continue;
					}
					Long probId = Long.parseLong(strProbId);
					
					//id로 Problem 정보 검색
					Problem findProblem = problemServiceETest.findOne(probId);
					
					//id로 이미지 정보 검색 
					List<String> ImgJsonToStrList = imageFileServerApiComponentETest.getImgJsonToStrListByProbIDServiceComponent(probId);
					String imgJsonObjectNormToString = ImgJsonToStrList.get(0);
//					String imgJsonObjectDarkToString = ImgJsonToStrList.get(1);
					//
					 
					//엔터티 -> DTO 변환
//					SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DD HH:MM:SS");
//					sdf.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
//					String dateResult = sdf.format(findProblem.getCreateDate());
//					System.out.println(dateResult);
//					System.out.println(findProblem.getCreateDate().getTime());
					BaseProblemDTO collect = new BaseProblemDTO(
							findProblem.getProbID().toString(), findProblem.getAnswerType(),
							findProblem.getQuestion(), findProblem.getSolution(),
							findProblem.getDifficulty(), findProblem.getCategory(),
							findProblem.getImgSrc(), findProblem.getTimeReco(),
							findProblem.getCreatorId(), new Timestamp( findProblem.getCreateDate().getTime()),
							findProblem.getValiatorID(), new Timestamp(findProblem.getValiateDate().getTime()),
							findProblem.getEditorID(), new Timestamp(findProblem.getEditDate().getTime()),
							findProblem.getSource(), findProblem.getIntention(),
							findProblem.getQuestionInitial(),findProblem.getSolutionInitial(),
							imgJsonObjectNormToString,null,
							null,null
							);

					outputBase.setProblem(collect);
					
					TestProblem testProblem = testProblemServiceETest.findOne( probId );
					BaseTestProblemDTO baseTestProblemDTO 
						= new BaseTestProblemDTO( testProblem.getProbID().toString(), testProblem.getPart().getPartID().toString()
								, testProblem.getSubject(), testProblem.getStatus() );
					outputBase.setTestProblem(baseTestProblemDTO);
					
					output.getTestProblems().add(outputBase);
					
				}
				return output;

	}
	
	
	/**
	 * 문제 업데이트 problem Update component
	 * 
	 */
	public String updateProblemcomponent(PutTestProblemDTOIn request) throws Exception{
		try {
						
			// 문제 n개 업데이트
			Long idx = -1L;
			if(request.getTestproblems() != null)
			for(BaseTestProblemandProblemDTO requestInfo__ : request.getTestproblems()) {
				idx++;
				Long LongProbId = Long.parseLong(requestInfo__.getProblem().getProbID());
				if( "출제".equals(requestInfo__.getTestProblem().getStatus()) || "보류".equals(requestInfo__.getTestProblem().getStatus())) {
					requestInfo__.getProblem().setValidatorID("T");
					if(requestInfo__.getProblem().getProbID()==null) {
						requestInfo__.getProblem().setProbID( requestInfo__.getTestProblem().getProbID() );
					}
				}
				
				//problem table
				problemServiceETest.problemUpdate( request.getUserID() ,requestInfo__.getProblem());
				
				//
				if(requestInfo__.getProblem().getProbID() !=null) {
					requestInfo__.getTestProblem().setProbID( requestInfo__.getProblem().getProbID() );
				}
				testProblemServiceETest.testProblemUpdate(request.getUserID(), requestInfo__.getTestProblem());
				
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
				
				imageFileServerApiComponentETest.deleteImgTempFolerOfUserIDServiceComponent(request.getUserID());
				
			}
			
			
			return "success";
		}catch(Exception e) {
			 e.printStackTrace(); 
			 return "fail";		
		}
	}
	
	/**
	 *  삭제
	 *  
	 */
	public String deleteProbComponent(String userId,Long probId) {
		
		try {
			
			if(userId==null || probId==null ) return null;
//			 실제 데이터 삭제 
			
			problemServiceETest.problemDeleteById(probId);
			testProblemServiceETest.testProblemDeleteById(probId);	
			
			imageFileServerApiComponentETest.deleteImgSrcFileOfProbIDServiceComponent(probId);
			imageFileServerApiComponentETest.deleteImgSrcFolerOfProbIDServiceComponent(probId);
			imageFileServerApiComponentETest.deleteImgTempFolerOfUserIDServiceComponent(userId);
	
			return "success";
			
		}catch(Exception e) {
			throw e;
		}
		
	}

	
	
	
}
