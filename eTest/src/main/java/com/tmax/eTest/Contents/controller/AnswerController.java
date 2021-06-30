package com.tmax.eTest.Contents.controller;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.tmax.eTest.Contents.exception.problem.NoDataException;
import com.tmax.eTest.Contents.service.AnswerServices;
import com.tmax.eTest.Contents.service.ProblemServices;
import reactor.core.publisher.Mono;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;



@CrossOrigin(origins="*")
@RestController
public class AnswerController {
	
	@Autowired
	AnswerServices answerServices;
	
	@Autowired
	ProblemServices problemService;
	
	@PostMapping(value="problems/{id}/answer-check", produces = "application/json; charset=utf-8")
	public int checkAnswer(@PathVariable("id") Integer id, @RequestParam String answer, @RequestBody String lrsbody) throws Exception {
		Map<String, Object> data = null;
		data = answerServices.getProblemSolution(id);
		String inputString = data.get("solution").toString();
		
		String bug = inputString.substring(0,inputString.indexOf(","));
		String jd = bug.replaceAll("\\[", "");
		
		

		String temp = jd.substring(jd.length()-2).replaceAll("\\]", "");
		
//		JSONParser parser = new JSONParser();
//		JSONObject obj = (JSONObject)parser.parse(inputString);
//		
//		String temp = obj.get("answer").toString().replaceAll("\\[", "");
//		temp = temp.replaceAll("\\]",""); //data가 다시 JSON 형태로 돌려지면 이거 그대로 복구 
		
		final String LRSServerURI = "http://192.168.153.132:8080";
		//header setting try 
		
		Charset utf8 = Charset.forName("UTF-8");
		MediaType mediaType = new MediaType("application", "json", utf8);
		WebClient client = WebClient
				.builder()
					.baseUrl(LRSServerURI + "/SaveStatementList")
					.defaultHeader(HttpHeaders.CONTENT_TYPE, mediaType.toString())
				.build();
		
		
		
		
		if(temp.equals(answer)) {
			String body = lrsbody;
			StringBuffer sb = new StringBuffer();
			sb.append(body);
			sb.replace(105,120, "\"isCorrect\":1,");
			
			Mono<String> response = client.post().body(BodyInserters.fromValue(sb.toString())).retrieve().bodyToMono(String.class);
			
			response.subscribe();
			
			return 1;
		} else {
			Mono<String> response = client.post().body(BodyInserters.fromValue(lrsbody)).retrieve().bodyToMono(String.class);

			response.subscribe();
			return 0;
		}
	}
	
	@GetMapping(value="/problems/{id}/solution", produces = "application/json; charset=utf-8")
	public Map<String, Object> problem(@PathVariable("id") Integer id) throws Exception{
		Map<String, Object> output = new HashMap<String, Object>();
		Map<String, Object> data = null;
		try {
			data = answerServices.getSolutionMaterial(id);
			output.put("resultMessage", "success");
			output.put("data", data);
		}catch(NoDataException e) {
			output.put("resultMessage", "Failed: "+e.getMessage());
		}
		
		return output;
	}
	
	@PostMapping(value="/test", consumes="application/json", produces = "application/json")
	public boolean test(
			@RequestBody String input
			) 
	{
		final String LRSServerURI = "http://192.168.153.132:8080";
		//header setting try 
		
		Charset utf8 = Charset.forName("UTF-8");
		MediaType mediaType = new MediaType("application", "json", utf8);
		WebClient client = WebClient
				.builder()
					.baseUrl(LRSServerURI + "/SaveStatement")
					.defaultHeader(HttpHeaders.CONTENT_TYPE, mediaType.toString())
				.build();
		
		Mono<String> response = client.post().body(BodyInserters.fromValue(input)).retrieve().bodyToMono(String.class);
		
		response.subscribe();

		
		return true;
	}

}
