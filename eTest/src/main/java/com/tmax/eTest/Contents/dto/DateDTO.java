package com.tmax.eTest.Contents.dto;

import java.sql.Date;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class DateDTO {
  @NotBlank
  private Date dateFrom;
  @NotBlank
  private Date dateTo;
}
