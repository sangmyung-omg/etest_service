package com.tmax.eTest.Contents.controller;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
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
@PropertySource("classpath:application.properties")
@RestController
public class AnswerController {
	
	@Autowired
	AnswerServices answerServices;
	
	@Autowired
	ProblemServices problemService;

	@Value("${etest.recommend.lrs.host}")
	private String LRS_HOST;
	
	@Value("${etest.recommend.lrs.port}")
	private String LRS_PORT;
	
	@PostMapping(value="problems/{id}/answer-check", produces = "application/json; charset=utf-8")
	public int checkAnswer(@PathVariable("id") Integer id, @RequestParam String answer, @RequestBody String lrsbody) throws Exception {
		Map<String, Object> data = null;
		data = answerServices.getProblemSolution(id);
		String inputString = data.get("solution").toString();
		String bug = inputString.substring(0,inputString.indexOf(","));
		String jd = bug.replaceAll("\\[", "");
		String temp = jd.substring(jd.length()-2).replaceAll("\\]", "");
		temp = temp.replace(" ", "");
		
//		final String LRSServerURI = "http://192.168.153.132:8080";		
		final String LRSServerURI = "http://" + LRS_HOST + ":" + LRS_PORT;
		
		Charset utf8 = Charset.forName("UTF-8");
		MediaType mediaType = new MediaType("application", "json", utf8);
		WebClient client = WebClient
				.builder()
					.baseUrl(LRSServerURI + "/SaveStatementList")
					.defaultHeader(HttpHeaders.CONTENT_TYPE, mediaType.toString())
				.build();
		
		if(temp.equals(answer)) {
			String body = lrsbody;
			int end=0;
			int start = body.lastIndexOf("isCorrect")+8;
			while(true) {
				String buf = Character.toString(body.charAt(start));
				if(buf.equals(",")) {
					end = start;
					break;
				}
				start++;
			}

			StringBuilder changebody = new StringBuilder(body);
			changebody.setCharAt(end-1, '1');
			Mono<String> response = client.post().body(BodyInserters.fromValue(changebody.toString())).retrieve().bodyToMono(String.class);
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

}