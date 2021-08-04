package com.tmax.eTest.Common.model.video;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VideoJoin {
  private Video video;
  private String userUuid;
}
