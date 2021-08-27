package com.tmax.eTest.Common.model.wiki;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.tmax.eTest.Common.model.uk.UkMaster;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@IdClass(WikiUkRelId.class)
public class WikiUkRel {
  @Id
  private Long wikiId;
  @Id
  private Long ukId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "wikiId", insertable = false, updatable = false, nullable = true)
  private Wiki wiki;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "ukId", insertable = false, updatable = false, nullable = true)
  private UkMaster ukMaster;

}
