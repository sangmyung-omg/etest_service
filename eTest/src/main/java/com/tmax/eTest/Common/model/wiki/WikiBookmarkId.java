package com.tmax.eTest.Common.model.wiki;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WikiBookmarkId implements Serializable {
  private String userUuid;
  private Long wikiId;
}
