package com.tmax.eTest.Common.model.meta;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MetaCodeMaster {
  @Id
  private String metaCodeId;
  private String domain;
  private String code;
  private String codeName;
}
