package com.tmax.eTest.Report.util;

import java.text.ParseException;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class TritonAPIManager {

	private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

	private final String HOST = "http://192.168.153.212:8003/v2/models/kt-rule/versions/1/infer";
	
	public void getInfer(TritonRequestDTO input) throws ParseException {
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
		
		input.initForDummy();
		
		TritonResponseDTO dto  =  webClient.post()
			  .bodyValue(input)
			  .retrieve()
			  .onStatus(HttpStatus::is4xxClientError, __ -> Mono.error(new Exception("triton 400 error")))
			  .onStatus(HttpStatus::is5xxServerError, __ -> Mono.error(new Exception("triton 500 error")))
			  .bodyToMono(TritonResponseDTO.class)
			  .block();
		
		 logger.info(dto.toString());
	}

}
