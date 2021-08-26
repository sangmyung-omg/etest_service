package com.tmax.eTest.Common.model.stat;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@IdClass(HitStatId.class)
public class HitStat {
  @Id
  private Date statDate;
  @Id
  private Long hitStatId;
  private Long videoHit;
  private Long bookHit;
  private Long wikiHit;
  private Long articleHit;
}
