package com.tmax.eTest.Contents.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class BookDTO {
  private Long bookId;
  private String bookSrc;
  private String title;
  private String createDate;
  private String creatorId;
  private String imgSrc;
  private String description;
  private Integer hit;
  private boolean bookmark;
}
