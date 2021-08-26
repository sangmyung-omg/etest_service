package com.tmax.eTest.Contents.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LRSGetStatementDTO {
  private List<String> actionTypeList;
  private List<String> sourceTypeList;
  private List<String> userIdList;
  private String dateFrom;
  private String dateTo;
}
