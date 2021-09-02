package com.tmax.eTest.Contents.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecommendDTO {
  private Boolean recommended;
  private ListDTO.Video recommends;
}
