package com.tmax.eTest.Contents.util;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.tmax.eTest.LRS.dto.GetStatementInfoDTO;
import com.tmax.eTest.LRS.dto.StatementDTO;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component(value = "LRSUtils")
public class LRSUtils {

  private static final String PLATFORM = "Kofia";

  public static enum ACTION_TYPE {
    enter, quit;
  }

  public static enum SOURCE_TYPE {
    video, textbook, wiki, article;
  }

  public StatementDTO makeStatement(String userId, String sourceId, ACTION_TYPE actionType, SOURCE_TYPE sourceType) {
    // String timestamp =
    // ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    String timestamp = LocalDateTime.now().toString();
    log.info("SAVE TIME : " + timestamp);
    return StatementDTO.builder().userId(userId).sourceId(sourceId).actionType(actionType.name())
        .sourceType(sourceType.name()).platform(PLATFORM).timestamp(timestamp).build();
  }

  public StatementDTO makeStatement(String userId, String sourceId, ACTION_TYPE actionType, SOURCE_TYPE sourceType,
      Integer duration) {
    // String timestamp =
    // ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    String timestamp = LocalDateTime.now().toString();
    log.info("SAVE TIME : " + timestamp);
    return StatementDTO.builder().userId(userId).sourceId(sourceId).actionType(actionType.name())
        .sourceType(sourceType.name()).platform(PLATFORM).timestamp(timestamp).duration(Integer.toString(duration))
        .build();
  }

  public GetStatementInfoDTO makeGetStatement(List<String> userIdList) {
    List<String> actionTypeList = Arrays.asList(ACTION_TYPE.enter.toString());
    List<String> sourceTypeList = Stream.of(SOURCE_TYPE.values()).map(Enum::name).collect(Collectors.toList());
    return GetStatementInfoDTO.builder().actionTypeList(actionTypeList).sourceTypeList(sourceTypeList)
        .userIdList(userIdList).build();
  }

  public GetStatementInfoDTO makeGetStatement(Timestamp dateFrom, Timestamp dateTo) {
    log.info("DateFrom: " + dateFrom + " DateTo: " + dateTo);
    List<String> actionTypeList = Arrays.asList(ACTION_TYPE.enter.toString());
    List<String> sourceTypeList = Stream.of(SOURCE_TYPE.values()).map(Enum::name).collect(Collectors.toList());
    return GetStatementInfoDTO.builder().actionTypeList(actionTypeList).sourceTypeList(sourceTypeList)
        .dateFromObj(dateFrom).dateToObj(dateTo).build();
  }
}
