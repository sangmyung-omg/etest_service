package com.tmax.eTest.Common.model.stat;

import java.io.Serializable;
import java.sql.Date;

import lombok.Data;

@Data
public class HitStatId implements Serializable {
  private Date statDate;
  private Long hitStatId;
}
