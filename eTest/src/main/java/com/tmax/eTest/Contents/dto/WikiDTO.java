package com.tmax.eTest.Contents.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WikiDTO {

  private Long wikiId;
  private String title;
  private String createDate;
  private String creatorId;
  private String description;
  private String summary;
  private String source;
  private Boolean bookmark;
  private List<String> uks;

}
