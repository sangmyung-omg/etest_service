package com.tmax.eTest.Contents.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BookDTO {
  private Long bookId;
  private String bookSrc;
  private String title;
  private String createDate;
  private String creatorId;
  private String imgSrc;
  private String description;
  private boolean bookmark;
}
