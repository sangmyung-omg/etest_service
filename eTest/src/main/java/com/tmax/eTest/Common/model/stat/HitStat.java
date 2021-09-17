package com.tmax.eTest.Common.model.stat;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@SequenceGenerator(name = "HIT_STAT_SEQ_GENERATOR", sequenceName = "HIT_STAT_SEQ", initialValue = 1, allocationSize = 1)
public class HitStat {
  private Date statDate;
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HIT_STAT_SEQ_GENERATOR")
  private Long hitStatId;
  private Long videoHit;
  private Long bookHit;
  private Long wikiHit;
  private Long articleHit;
}
