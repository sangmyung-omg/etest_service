package com.tmax.eTest.TestStudio.controller.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.SystemMenuBar;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tmax.eTest.Common.model.problem.DiagnosisProblem;
import com.tmax.eTest.Common.model.problem.ProblemChoice;
import com.tmax.eTest.Common.model.uk.ProblemUKRelation;
import com.tmax.eTest.TestStudio.controller.component.TestProblemApiComponentTs;
import com.tmax.eTest.TestStudio.dto.problems.base.BaseProbChoiceDTO;
import com.tmax.eTest.TestStudio.dto.problems.base.BaseTestProblemSetDTO;
import com.tmax.eTest.TestStudio.dto.problems.in.GetProblemDTOIn;
import com.tmax.eTest.TestStudio.dto.problems.in.PostTestProblemDTOIn;
import com.tmax.eTest.TestStudio.dto.problems.out.GetTestProblemDTOOut;
import com.tmax.eTest.TestStudio.repository.ProbChoiceQRepositoryTs;
import com.tmax.eTest.TestStudio.service.DiagProblemServiceTs;
import com.tmax.eTest.TestStudio.service.ProbChoiceServiceTs;
import com.tmax.eTest.TestStudio.service.ProbUKRelServiceTs;
import com.tmax.eTest.TestStudio.util.InitialConsonantUtilTs;
import com.tmax.eTest.TestStudio.util.PathUtilTs;

import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class TestControllerETest {
	

	private final TestProblemApiComponentTs problemApiComponent;
	private final DiagProblemServiceTs diagProblemServiceETest;
	private final TestProblemQRepositoryETest tempTestRepository; 
	private final ProbChoiceServiceTs probChoiceServiceETest;
	private final ProbUKRelServiceTs probUKRelServiceETest;
	private final ProbChoiceQRepositoryTs probChoiceQRepositoryETest;
	private final PathUtilTs pathUtilTs;
	private PathUtilTs testP = new PathUtilTs();
	private String dirPath = testP.getDirPath();
	private final InitialConsonantUtilTs initialConsonantTs;
	
///////////////////////////////////////////////////////////////////
//	
//	
//	/**
//	 * 등록
//	 * new ResponseEntity<>(probGetResponse, HttpStatus.ACCEPTED);
//	 */
//	@PostMapping("/ETest1")
//	public ResponseEntity<String> CreateProblem(
////			@RequestBody CreateDiagProblemRequest request
//			) {
//		
//		try {
//			System.out.println("s");
//			
//			DiagnosisProblem diagnosisProblem = diagProblemServiceETest.findByIdOrderSorted(1L).get(0); 
//			System.out.println ( diagnosisProblem.getOrderNum() );
//			diagnosisProblem = diagProblemServiceETest.findByIdOrderSorted(1L).get(1); 
//			System.out.println ( diagnosisProblem.getOrderNum() );
//			
//			
//			
//			return new ResponseEntity<>("res", HttpStatus.OK);
//			
//		}catch (Exception e) {
//			e.printStackTrace();
//			return null;
//		}
//	}
//	
//	/**
//	 * 
//	 * qdsl
//	 */
//	@GetMapping("/qdsl1")
//	public ResponseEntity<String> qdslTest(
////			@RequestBody CreateDiagProblemRequest request
//			) {
//		
//		try {
//			
//			String output = tempTestRepository.searchLatestProblem().getProbID().toString();
//			
//			return new ResponseEntity<>(output, HttpStatus.OK);
//			
//		}catch (Exception e) {
//			e.printStackTrace();
//			return null;
//		}
//	}
//	@GetMapping("/qdsl2")
//	public ResponseEntity<String> qdslTest2(
////			@RequestBody CreateDiagProblemRequest request
//			) {
//		
//		try {
//			
//			String output = tempTestRepository.searchLatestProblem().getProbID().toString();
//			
//			return new ResponseEntity<>(output, HttpStatus.OK);
//			
//		}catch (Exception e) {
//			e.printStackTrace();
//			return null;
//		}
//	}
//	@GetMapping("/qdsl3")
//	public ResponseEntity<String> qdslTest3(
////			@RequestBody CreateDiagProblemRequest request
//			) {
//		
//		try {
//			
//			String output = tempTestRepository.searchLatestProblemIDTest().toString();
//			
//			return new ResponseEntity<>(output, HttpStatus.OK);
//			
//		}catch (Exception e) {
//			e.printStackTrace();
//			return null;
//		}
//	}
//	//problemchoice
//	@GetMapping("/pbCD")
//	public ResponseEntity<String> probChoiceDelTest(
////			@RequestBody CreateDiagProblemRequest request
//			) {
//		
//		try {
//			Long output = probChoiceQRepositoryETest.probChoiceDeleteByProbId(2000);
//			
//			return new ResponseEntity<>(output.toString(), HttpStatus.OK);
//			
//		}catch (Exception e) {
//			e.printStackTrace();
//			return null;
//		}
//	}
//	//
//	@GetMapping("/pbCG")
//	public ResponseEntity<String> probChoiceGetTest(
////			@RequestBody CreateDiagProblemRequest request
//			) {
//		
//		try {
//			List<ProblemChoice> result = probChoiceServiceETest.findAllByProbId(1001L);
//			List<String> output = new ArrayList<String>();
//			for(ProblemChoice pc : result ) {
//				output.add( String.valueOf( pc.getChoiceNum() ) );
//				System.out.println(pc.getChoiceNum());
//			}
//			
//			return new ResponseEntity<>(output.toString(), HttpStatus.OK);
//			
//		}catch (Exception e) {
//			e.printStackTrace();
//			return null;
//		}
//	}
//	@PutMapping("/pbCU")
//	public ResponseEntity<String> probChoicePutTest(
//			@RequestBody PostTestProblemDTOIn request
//			) {
//		
//		try {
//			probChoiceServiceETest.probChoiceUpdate("testuser", request.getTestProblems().get(0).getProbChoices(),2019L);
//			List<String> output = new ArrayList<String>();
//			
//			
//			return new ResponseEntity<>(output.toString(), HttpStatus.OK);
//			
//		}catch (Exception e) {
//			e.printStackTrace();
//			return null;
//		}
//	}
//	@GetMapping("/pbUKRG")
//	public ResponseEntity<String> probUKRGetTest(
////			@RequestBody CreateDiagProblemRequest request
//			) {
//		
//		try {
//			
//			List<ProblemUKRelation> result = probUKRelServiceETest.findAllByProbId(1001L);
//			List<String> output = new ArrayList<String>();
//			for(ProblemUKRelation pc : result ) {
//				output.add( String.valueOf( pc.getUkId() ) );
//				System.out.println(pc.getUkId());
//			}
//			
//			return new ResponseEntity<>(output.toString(), HttpStatus.OK);
//			
//		}catch (Exception e) {
//			e.printStackTrace();
//			return null;
//		}
//	}
//	@GetMapping("/jsonP")
//	public ResponseEntity<String> jsonParsing(
////			@RequestBody CreateDiagProblemRequest request
//			) {
//		
//		try {
//			
//			String output = initialConsonantTs.InitialConsonantsV2(
//					"[{\"data\":\"3년 간 주식투자로 다음과 같은 현금흐름이 발생했을 때, 보유기간 동안 수익률로 올바른 것은?\",\"type\":\"QUESTION_TEXT\"},{\"data\":\"주식 매입 금액 : 100만원\\n주식 매도 금액 : 120만원\\n배당금 : 10만원\\n비용 : 5만원\",\"type\":\"PREFACED_EXAMPLE_BOX_TEXT\"},{\"type\":\"MULTIPLE_CHOICE_TEXT\",\"data\":[\"20%\",\"25%\",\"30%\",\"35%\"]}]"
//					);
//			System.out.println(output);
//			return new ResponseEntity<>(output, HttpStatus.OK);
//			
//		}catch (Exception e) {
//			e.printStackTrace();
//			return null;
//		}
//	}
//	@GetMapping("/imagePathTest")
//	public ResponseEntity<String> imagePathTest(
////			@RequestBody CreateDiagProblemRequest request
//			) {
//		
//		try {
//			System.out.println( dirPath );
//			System.out.println( pathUtilTs.getDefaultPath() );
//			System.out.println( pathUtilTs.getDirPath() );
//			
//			String rtName = "";
//			String jsonStr = "[{\"data\":\"투자에서 발생하는 수익률은 다음과 같이 계산한다. 먼저 주식과 채권과 같은 투자 자산을 매도한 가격에서 이를 매입할 때 들어간 금액을 뺀 시세 차익과 채권 이자와 주식 배당처럼 투자 자산에서 발생한 현금흐름을 더한다. 그리로 수수료 등 비용을 차감한다. 이렇게 계산한 금액을 매입 금액으로 나눈 다음 100을 곱해 퍼센트(%)로 환산하면 수익률을 계산할 수 있다. \\n\\n수익률=(매도 금액-매입 금액±보유 기간 중 현금흐름-비용)/(매입 금액)  ×100\\n          =  (120만원-100만원+10만원-5만원)/100만원  ×100 = 25% \",\"type\":\"SOLUTION_TEXT\"},{\"type\":\"MULTIPLE_CHOICE_IMAGE\",\"dataType\":\"image\",\"data\":[\"test4.png\",\"test5.png\",\"test6.png\"]},{\"data\":[\"2\"],\"type\":\"MULTIPLE_CHOICE_CORRECT_ANSWER\"}]";
//			
//			JSONParser jsonParser = new JSONParser();
//			
//			 JSONArray jsonArray = (JSONArray) jsonParser.parse(jsonStr);
//
//			 for(int i=0; i< jsonArray.size() ; i++) {
//				 
//				 JSONObject jsonObj = (JSONObject) jsonArray.get(i);
//				 if( jsonObj.containsKey("data") ) {
//					 if( jsonObj.get("data").getClass().getName() == "java.lang.String" ) {
//						 String tempData = (String) jsonObj.get("data");
////						 tempData = tempData.replaceAll( System.getProperty("line.separator").toString(), " ");
//						 tempData = tempData.replaceAll("(\r\n|\r|\n|\n\r)", " ");
//						 
//						 rtName = rtName + initialConsonantTs.InitialConsonants(tempData);
//						 rtName = rtName + " ";
//						 
//					 }else if( jsonObj.get("data").getClass().getName() == "org.json.simple.JSONArray" ) {
////						 System.out.println("testttttttttttttest");
////						 System.out.println( jsonObj.get("data").getClass().arrayType() ); // 특정인만 error? why?
//						 
//						 ArrayList<Object> tempArrayList = (ArrayList<Object>) jsonObj.get("data");
////						 ArrayList<String> tempArrayList = (ArrayList<String>) jsonObj.get("data");
//						 for(Object a : tempArrayList)
//						 System.out.println( tempArrayList.get(0).toString() ) ;
//						 System.out.println( tempArrayList.get(0).toString().getClass().getName() ) ;
////						 for(String tempData : tempArrayList) {
////							 
//////							 tempData = tempData.replaceAll( System.getProperty("line.separator").toString(), " ");
////							 tempData = tempData.replaceAll("(\r\n|\r|\n|\n\r)", " ");
////							 
////							 rtName = rtName + initialConsonantTs.InitialConsonants(tempData);
////							 rtName = rtName + " ";
////							
////						 }
//					 }
//				 }
//			 }
//			
//			
//			String output = "-";
//			return new ResponseEntity<>(output, HttpStatus.OK);
//			
//		}catch (Exception e) {
//			e.printStackTrace();
//			return null;
//		}
//	}
//	
//	
//	@GetMapping("/collectTest")
//	public ResponseEntity<String> feffTest(
//			) {
//		
//		try {
//			List<Long> PC_CN=
//					probChoiceServiceETest.findAllByProbId(1L).stream()
////						.map( x-> Long.valueOf(x.getChoiceNum()) )
//						.map( x-> x.getChoiceNum() )
//						.collect(Collectors.toList());
//			
//			for(Long i: PC_CN) {
//				System.out.println(i);
//			}
//
////			Long j = 1L;
//			long j = 1;
//			if(PC_CN.contains(j)) {
//				System.out.println("contains");
//			}
//			
//			String output = "ok";
//			return new ResponseEntity<>(output, HttpStatus.ACCEPTED);
//		} catch(Exception e) {
//			e.printStackTrace();
//			return new ResponseEntity<>("no", HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//
//	}
//	
//	
//	
///////////////////////////////////////////////////////////////////
	
}
