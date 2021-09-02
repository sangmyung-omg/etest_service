package com.tmax.eTest.Common.model.video;

import java.io.Serializable;

import lombok.Data;

@Data
public class VideoUkRelId implements Serializable {
  private String videoId;
  private Long ukId;
}
