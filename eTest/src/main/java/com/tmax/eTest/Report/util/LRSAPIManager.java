package com.tmax.eTest.Report.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tmax.eTest.LRS.dto.GetStatementInfoDTO;
import com.tmax.eTest.LRS.dto.StatementDTO;
import com.tmax.eTest.Report.dto.ProblemSolveListDTO;

/**
 * Call StatementList GET API from LRS Server
 * 
 * @author sangheonLee
 */
@Component
@PropertySource("classpath:application.properties")
public class LRSAPIManager {

	private final Logger logger = LoggerFactory.getLogger("LRSAPIManager");

	//기본값. 초기화시 변경 됨.
	private String HOST = "http://192.168.153.132:8080";
//	private static final String HOST = System.getenv("LRS_HOST");

	public List<Integer> saveStatementList(List<StatementDTO> input) throws ParseException {
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
							.baseUrl(HOST)
							.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
							.clientConnector(new ReactorClientHttpConnector(httpClient))
							.build();
		
		logger.info(input.toString());
		
		List<Integer> info =  webClient.post()
				  .uri("/SaveStatementList")
				  .bodyValue(input)
				  .retrieve()
				  //.onStatus(HttpStatus::is4xxClientError, __ -> Mono.error(new GenericInternalException("ERR-LRS-400", "LRS 400 error")))
				  //.onStatus(HttpStatus::is5xxServerError, __ -> Mono.error(new GenericInternalException("ERR-LRS-500", "LRS 500 error")))
				  .bodyToFlux(Integer.class)
				  .collectList()
				  .block();
		
		return info;
	}

	public List<StatementDTO> getStatementList(GetStatementInfoDTO input) throws ParseException {
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
							.baseUrl(HOST)
							.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
							.clientConnector(new ReactorClientHttpConnector(httpClient))
							.build();
		
		logger.info(input.toString());
		
		List<StatementDTO> info =  webClient.post()
				  .uri("/StatementList")
				  .bodyValue(input)
				  .retrieve()
				  //.onStatus(HttpStatus::is4xxClientError, __ -> Mono.error(new GenericInternalException("ERR-LRS-400", "LRS 400 error")))
				  //.onStatus(HttpStatus::is5xxServerError, __ -> Mono.error(new GenericInternalException("ERR-LRS-500", "LRS 500 error")))
				  .bodyToFlux(StatementDTO.class)
				  .collectList()
				  .block();
		
		return info;
	}

	@Autowired
	public LRSAPIManager(
			@Value("${etest.recommend.lrs.host}") String IP, 
			@Value("${etest.recommend.lrs.port}") String PORT) {
		logger.info("constructor" + IP + PORT);
		
		if(IP != null && PORT != null)
			this.HOST = String.format("http://%s:%s", IP, PORT);
	}

	public LRSAPIManager() {
	}


	/**
	 * Method to call update mastery lrs info from lrs
	 * @author Jonghyun Seong
	 * @since 2021-06-16
	 */
	public ProblemSolveListDTO getInfoForMastery(String token) {
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
							.baseUrl(HOST)
							.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
							.clientConnector(new ReactorClientHttpConnector(httpClient))
							.build();
		
		
		//Call post to "/InfoForMastery" LRS server --> get as String
		Mono<String> info =  webClient.get()
									  .uri("/InfoForMastery")
									  .header("token", token)
									  .retrieve()
									  //.onStatus(HttpStatus::is4xxClientError, __ -> Mono.error(new GenericInternalException("ERR-LRS-400", "LRS 400 error")))
									  //.onStatus(HttpStatus::is5xxServerError, __ -> Mono.error(new GenericInternalException("ERR-LRS-500", "LRS 500 error")))
									  .bodyToMono(String.class);

		//Convert output to result
		ProblemSolveListDTO result = null;
		try {
			result = new ObjectMapper().readValue(info.block(), ProblemSolveListDTO.class);
		}
		catch (Throwable e) {
			logger.warn("LRS return body : " + info.block());
		}

		return result;
	}
}
