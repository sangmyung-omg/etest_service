package com.tmax.eTest.Common.model.video;

import java.io.Serializable;

import lombok.Data;

@Data
public class VideoHashtagId implements Serializable {
  private Long videoId;
  private Long hashtagId;
}
