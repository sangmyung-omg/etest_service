package com.tmax.eTest.Contents.util;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.tmax.eTest.Contents.dto.LRSGetStatementDTO;
import com.tmax.eTest.Contents.dto.LRSStatementDTO;
import com.tmax.eTest.Contents.exception.ContentsException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

@Slf4j
@Component(value = "LRSService")
public class LRSService {

  private String LRS_API_BASE;
  private String LRS_API;
  private HttpClient httpClient;
  private WebClient webClient;

  private static final String LRS_HTTP = "http://";
  private static final String COLON = ":";
  private static final String PLATFORM = "Kofia";

  public static enum ACTION_TYPE {
    enter;
  }

  public static enum SOURCE_TYPE {
    video, textbook, wiki, article;
  }

  public LRSService(@Value("${etest.recommend.lrs.host}") String LRS_HOST,
      @Value("${etest.recommend.lrs.port}") String LRS_PORT) {
    this.LRS_API_BASE = new StringBuilder(LRS_HTTP).append(LRS_HOST).append(COLON).append(LRS_PORT).toString();
  }

  public void init(String suffix) {
    this.LRS_API = this.LRS_API_BASE + suffix;
    log.info(this.LRS_API);
    buildClient();
  }

  private void buildClient() {
    this.httpClient = HttpClient.create().option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
        .responseTimeout(Duration.ofMillis(5000))
        .doOnConnected(conn -> conn.addHandlerLast(new ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS))
            .addHandlerLast(new WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS)));

    this.webClient = WebClient.builder().baseUrl(this.LRS_API)
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .clientConnector(new ReactorClientHttpConnector(httpClient)).build();
  }

  public List<LRSStatementDTO> getStatementList(LRSGetStatementDTO lrsGetStatementDTO) {
    // Mono<LRSStatementDTO[]> responses =
    // webClient.post().bodyValue(lrsGetStatementDTO).retrieve()
    // .onStatus(HttpStatus::is4xxClientError, __ -> Mono.error(new
    // ContentsException("ERR-LRS-400", "LRS 400 error")))
    // .onStatus(HttpStatus::is5xxServerError, __ -> Mono.error(new
    // ContentsException("ERR-LRS-500", "LRS 500 error")))
    // .bodyToMono(LRSStatementDTO[].class);

    // List<LRSStatementDTO> result = Arrays.asList(responses.block());
    // return result;
    List<LRSStatementDTO> responses = webClient.post().bodyValue(lrsGetStatementDTO).retrieve()
        .onStatus(HttpStatus::is4xxClientError, __ -> Mono.error(new ContentsException("ERR-LRS-400", "LRS 400 error")))
        .onStatus(HttpStatus::is5xxServerError, __ -> Mono.error(new ContentsException("ERR-LRS-500", "LRS 500 error")))
        .bodyToFlux(LRSStatementDTO.class).toStream().collect(Collectors.toList());

    return responses;
  }

  public Boolean saveStatement(LRSStatementDTO lrsStatementDTO) {
    Boolean response = webClient.post().bodyValue(lrsStatementDTO).retrieve()
        .onStatus(HttpStatus::is4xxClientError, __ -> Mono.error(new ContentsException("ERR-LRS-400", "LRS 400 error")))
        .onStatus(HttpStatus::is5xxServerError, __ -> Mono.error(new ContentsException("ERR-LRS-500", "LRS 500 error")))
        .bodyToMono(Boolean.class).flux().toStream().findFirst().orElse(false);

    // response.subscribe(res -> log.info("Success: Save statement!"), e -> {
    // log.error("Error: Save Statement | " + e.getMessage());
    // throw new ContentsException(ErrorCode.LRS_ERROR, "SaveStatementAPI");
    // });
    return response;
  }

  public LRSStatementDTO makeStatement(String userId, String sourceId, ACTION_TYPE actionType, SOURCE_TYPE sourceType) {
    // String timestamp =
    // ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    String timestamp = LocalDateTime.now().toString();
    log.info("SAVE TIME : " + timestamp);
    return LRSStatementDTO.builder().userId(userId).sourceId(sourceId).actionType(actionType.name())
        .sourceType(sourceType.name()).platform(PLATFORM).timestamp(timestamp).build();
  }

  public LRSGetStatementDTO makeGetStatement(List<String> userIdList) {
    List<String> actionTypeList = Arrays.asList(ACTION_TYPE.enter.toString());
    List<String> sourceTypeList = Stream.of(SOURCE_TYPE.values()).map(Enum::name).collect(Collectors.toList());
    return LRSGetStatementDTO.builder().actionTypeList(actionTypeList).sourceTypeList(sourceTypeList)
        .userIdList(userIdList).build();
  }

  public LRSGetStatementDTO makeGetStatement() {
    LocalDate date = LocalDate.now();
    String dateFrom = date.toString();
    String dateTo = date.plusDays(1).toString();
    List<String> actionTypeList = Arrays.asList(ACTION_TYPE.enter.toString());
    List<String> sourceTypeList = Stream.of(SOURCE_TYPE.values()).map(Enum::name).collect(Collectors.toList());
    return LRSGetStatementDTO.builder().actionTypeList(actionTypeList).sourceTypeList(sourceTypeList).dateFrom(dateFrom)
        .dateTo(dateTo).build();
  }
}
