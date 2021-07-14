package com.tmax.eTest.Report.util;

import java.text.ParseException;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tmax.eTest.Report.dto.triton.TritonRequestDTO;
import com.tmax.eTest.Report.dto.triton.TritonResponseDTO;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

/**
 * Call StatementList GET API from LRS Server
 * 
 * @author sangheonLee
 */
@Component
@PropertySource("classpath:application.properties")
public class TritonAPIManager {

	private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

	private String TRITON_ADDR = "http://192.168.159.62:18500/v2/models/kt-rule/versions/1/infer";
	
	/**
	 * Added by Jonghyun seong. to get params from bean
	 * @since 2021-06-21
	 */
	@Autowired
	public TritonAPIManager(@Value("${etest.recommend.masterytriton.host}") String IP, 
							 @Value("${etest.recommend.masterytriton.port}")	String PORT,
							 @Value("${etest.recommend.masterytriton.modelname}") String MODEL_NAME, 
							 @Value("${etest.recommend.masterytriton.modelver}") String MODEL_VERSION){
		
		logger.info("constructor"  + IP+PORT);
		this.TRITON_ADDR = String.format("http://%s:%s/v2/models/%s/versions/%s/infer", IP, PORT, MODEL_NAME, MODEL_VERSION);
	}
	
	public TritonResponseDTO getInfer(TritonRequestDTO input) throws ParseException {
		//Create a http timeout handler
		HttpClient httpClient = HttpClient.create()
									.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
									.responseTimeout(Duration.ofMillis(5000))
									.doOnConnected(conn ->
										conn.addHandlerLast(new ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS))
											.addHandlerLast(new WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS))
									);

		//Create header
		WebClient webClient = WebClient.builder()
							.baseUrl(TRITON_ADDR)
							.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
							.clientConnector(new ReactorClientHttpConnector(httpClient))
							.build();
		
		TritonResponseDTO tritonResult = null;
		String payload = null;
		try {
			payload = new ObjectMapper().writeValueAsString(input);
		} catch (JsonProcessingException e) {
            e.printStackTrace();
        }
		
		logger.info(payload);
		if(payload != null)
		{
			try {	
				Mono<String> info  = webClient.post()
						  .body(Mono.just(payload), String.class)
						  .retrieve()
						  .onStatus(HttpStatus::is4xxClientError, __ -> Mono.error(new Exception("triton 400 error")))
						  .onStatus(HttpStatus::is5xxServerError, __ -> Mono.error(new Exception("triton 500 error")))
						  .bodyToMono(String.class);
				
				tritonResult = new ObjectMapper().readValue(info.block(), TritonResponseDTO.class);
				
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
		}
		
		return tritonResult;
	}
	
	public TritonResponseDTO getUnderstandingScoreInTriton(Map<String, List<Object>> probInfoForTriton) {
		// first process : 문제별 PK 얻어오기.

		TritonRequestDTO tritonReq = new TritonRequestDTO();

		tritonReq.initDefault();

		tritonReq.pushInputData("UKList", "INT32",
				probInfoForTriton.get(StateAndProbProcess.UK_LIST_KEY));
		tritonReq.pushInputData("IsCorrectList", "INT32",
				probInfoForTriton.get(StateAndProbProcess.IS_CORRECT_LIST_KEY));
		tritonReq.pushInputData("DifficultyList", "INT32",
				probInfoForTriton.get(StateAndProbProcess.DIFF_LIST_KEY));

		// Triton에 데이터 요청.
		TritonResponseDTO tritonResponse = null;
		try {
			tritonResponse = getInfer(tritonReq);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return tritonResponse;
	}

}
