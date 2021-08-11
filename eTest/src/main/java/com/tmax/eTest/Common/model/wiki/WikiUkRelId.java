package com.tmax.eTest.Common.model.wiki;

import java.io.Serializable;

import lombok.Data;

@Data
public class WikiUkRelId implements Serializable {
  private Long wikiId;
  private Long ukId;
}
