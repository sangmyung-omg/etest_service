package com.tmax.eTest.Common.model.book;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookBookmarkId implements Serializable {
  private String userUuid;
  private Long bookId;
}
