package com.tmax.eTest.TestStudio.controller.component;

import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.tmax.eTest.Common.model.problem.DiagnosisProblem;
import com.tmax.eTest.Common.model.problem.Problem;
import com.tmax.eTest.Common.model.problem.ProblemChoice;
import com.tmax.eTest.Common.model.problem.TestProblem;
import com.tmax.eTest.Common.model.uk.ProblemUKRelation;
import com.tmax.eTest.Common.model.uk.UkMaster;
import com.tmax.eTest.TestStudio.dto.problems.in.PostTestProblemDTOIn;
import com.tmax.eTest.TestStudio.dto.problems.in.PutDiagCurrStatusDTOIn;
import com.tmax.eTest.TestStudio.dto.problems.in.PutDiagProblemDTOIn;
import com.tmax.eTest.TestStudio.dto.problems.in.PutTestProbStatusDTOIn;
import com.tmax.eTest.TestStudio.dto.problems.in.PutTestProblemDTOIn;
import com.tmax.eTest.TestStudio.dto.problems.out.GetDiagProblemDTOOut;
import com.tmax.eTest.TestStudio.dto.problems.out.GetTestProblemDTOOut;
import com.tmax.eTest.TestStudio.controller.TestProblemControllerTs;
import com.tmax.eTest.TestStudio.controller.component.exception.CustomExceptionTs;
import com.tmax.eTest.TestStudio.controller.component.exception.ErrorCodeEnumTs;
import com.tmax.eTest.TestStudio.controller.component.exception.NoDataExceptionTs;
import com.tmax.eTest.TestStudio.dto.problems.base.BaseDiagCurriculumDTO;
import com.tmax.eTest.TestStudio.dto.problems.base.BaseProbChoiceDTO;
import com.tmax.eTest.TestStudio.dto.problems.base.BaseProbUKRelDTO;
import com.tmax.eTest.TestStudio.dto.problems.base.BaseProblemDTO;
import com.tmax.eTest.TestStudio.dto.problems.base.BaseProblemSetDTO;
import com.tmax.eTest.TestStudio.dto.problems.base.BaseTestProblemDTO;
import com.tmax.eTest.TestStudio.dto.problems.base.BaseTestProblemSetDTO;
import com.tmax.eTest.TestStudio.service.ProbChoiceServiceTs;
import com.tmax.eTest.TestStudio.service.ProbUKRelServiceTs;
import com.tmax.eTest.TestStudio.service.ProblemServiceTs;
import com.tmax.eTest.TestStudio.service.TestProblemServiceTs;
import com.tmax.eTest.TestStudio.service.UKServiceTs;
import com.tmax.eTest.TestStudio.util.PathUtilTs;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional(rollbackFor = {Exception.class})
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Slf4j
public class ProblemApiComponentTs {
	
	private final ProblemServiceTs problemServiceETest;
	private final ProbChoiceServiceTs probChoiceServiceETest;
	private final ProbUKRelServiceTs probUKRelServiceETest;
	private final UKServiceTs ukServiceETest;
	private final TestProblemServiceTs testProblemServiceETest;
	
	private final ImageFileServerApiComponentTs imageFileServerApiComponentETest;
	private final PathUtilTs pathUtilEtest;

	
	/**
	 * 문제 업데이트 problem Update component
	 * 
	 */
	public String updateBasicProblemcomponent(BaseProblemSetDTO baseProblemSetDTO, String userID  ) throws Exception{
					
			if(baseProblemSetDTO != null) {
				
				Long LongProbId = Long.parseLong(baseProblemSetDTO.getProblem().getProbID());
//				if( pathUtilEtest.getStatusOn().equals(requestInfo__.getTestProblem().getStatus()) || pathUtilEtest.getStatusOff().equals(requestInfo__.getTestProblem().getStatus())) {
//					requestInfo__.getProblem().setValidatorID("T");
//					if(requestInfo__.getProblem().getProbID()==null) {
//						requestInfo__.getProblem().setProbID( requestInfo__.getTestProblem().getProbID() );
//					}
//				}
				
				if(problemServiceETest.findOne(LongProbId).isPresent()) {				
					//
					problemServiceETest.problemUpdate( userID ,baseProblemSetDTO.getProblem());				
					//
					if(baseProblemSetDTO.getProbChoices()!=null) {
						if(!baseProblemSetDTO.getProbChoices().isEmpty()) {
							
							List<Long> PC_CN=
									probChoiceServiceETest.findAllByProbId(LongProbId).stream()
//										.map( x-> Long.valueOf(x.getChoiceNum()) )
										.map( x-> x.getChoiceNum() )
										.collect(Collectors.toList());
							
							for(BaseProbChoiceDTO PC : baseProblemSetDTO.getProbChoices()) {
								
								if( PC.getChoiceNum() == null ) continue;
								
								if( PC_CN.contains( Long.parseLong(PC.getChoiceNum())  ) ){
									
									probChoiceServiceETest.probChoiceUpdate_single(userID, PC, LongProbId);
									
									PC_CN.remove( Long.parseLong(PC.getChoiceNum()) );
									
								}else {
									
									ProblemChoice problemChoice = new ProblemChoice();
//									Problem problemTemp = new Problem();
//									problemTemp.setProbID(problem.getProbID());
//									problemChoice.setProbID(problemTemp);
									problemChoice.setProbID(problemServiceETest.findOne(LongProbId).get());
									problemChoice.setChoiceNum( Long.parseLong( PC.getChoiceNum() ) );
									
									UkMaster ukMasterTemp = new UkMaster();
									if( PC.getUkID() ==null ) {
										problemChoice.setUkId( null );
									}else {
										ukMasterTemp.setUkId(Integer.parseInt( PC.getUkID() ) );
										problemChoice.setUkId(ukMasterTemp);
//										problemChoice.setUkId(ukServiceETest.findOneByUKId( Long.parseLong(PC.getUkID()) ).get());
									}
									
									if(PC.getChoiceScore()==null) {
										problemChoice.setChoiceScore( null );
									}else {
										problemChoice.setChoiceScore( Integer.parseInt( PC.getChoiceScore() ) );
									}
									probChoiceServiceETest.probChoiceCreate(problemChoice);
								}
								
							}

							probChoiceServiceETest.probChoiceDeleteAllByProbIdAndChoiceNum(LongProbId, PC_CN);
							
						}
					}
					//	
					if(baseProblemSetDTO.getProbUKRels()!=null) {
						if(!baseProblemSetDTO.getProbUKRels().isEmpty()) {
							
							List<Long> PUR_UI=				
									probUKRelServiceETest.findAllWUKByProbId(LongProbId).stream()
										.map( x -> x.getUkId().getUkId().longValue())
										.collect(Collectors.toList());
							
							for(BaseProbUKRelDTO PUR : baseProblemSetDTO.getProbUKRels()) {
								
								if( PUR.getUkID() == null ) continue;
								
								if( PUR_UI.contains( Long.parseLong(PUR.getUkID())  ) ){
								
//									probUKRelServiceETest.probUKRelCreateUpdate_single(userID, PUR, LongProbId);
								
									PUR_UI.remove( Long.parseLong(PUR.getUkID()) );
									
								}else {
								
									ProblemUKRelation problemUKRelation = new ProblemUKRelation();
									problemUKRelation.setProbID(problemServiceETest.findOne(LongProbId).get());
									UkMaster ukMasterTemp = new UkMaster();
									ukMasterTemp.setUkId(Integer.parseInt( PUR.getUkID() ) );
									problemUKRelation.setUkId(ukMasterTemp);
									probUKRelServiceETest.probUKRelCreate(problemUKRelation);
								}
								
							}

							probUKRelServiceETest.probUKRelDeleteAllByProbIdAndUkIDs(LongProbId, PUR_UI);		
		
						}
					}
					// 		
					if(baseProblemSetDTO.getProblem().getImgSrcListIn()!=null
							&& baseProblemSetDTO.getProblem().getImgToEditSrcListIn()!=null) {	
						
						File folder = new File( pathUtilEtest.getDirPath()
												+ File.separator + baseProblemSetDTO.getProblem().getProbID() );
						
						if( folder.exists() ){
							List<String> willDelImgData = new ArrayList<String>();
							for( String imgSrc__ : folder.list() ) {
								willDelImgData.add(imgSrc__);
							}
							for(String src: baseProblemSetDTO.getProblem().getImgSrcListIn()) {
								willDelImgData.remove(src);
							}
							imageFileServerApiComponentETest.deleteImgSrcsOfProbIDServiceComponent(LongProbId, willDelImgData );
						}
//						Boolean isSuccess = imageFileServerApiComponentETest
//								.assignImgFileListServiceComponent(request.getUserID(), LongProbId, requestInfo__.getProblem().getImgSrcListIn());
						Boolean isSuccess = imageFileServerApiComponentETest
								.assignImgFileListServiceComponent(userID, LongProbId, baseProblemSetDTO.getProblem().getImgToEditSrcListIn());
					
					}
					
				}else {
					throw new NoDataExceptionTs("Problem",LongProbId.toString());
				}
							
			}
					
			return "success";

	}


}
