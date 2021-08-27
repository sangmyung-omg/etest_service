package com.tmax.eTest.Contents.dto;

import com.tmax.eTest.Common.model.wiki.Wiki;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WikiJoin {
  private Wiki wiki;
  private String userUuid;
}
