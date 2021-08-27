package com.tmax.eTest.Common.model.article;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleBookmarkId implements Serializable {
  private String userUuid;
  private Long articleId;
}
