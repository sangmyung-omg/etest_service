package com.tmax.eTest.Common.model.article;

import java.io.Serializable;

import lombok.Data;

@Data
public class ArticleUkRelId implements Serializable {
  private Long articleId;
  private Long ukId;
}
