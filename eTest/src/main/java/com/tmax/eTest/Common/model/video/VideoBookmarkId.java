package com.tmax.eTest.Common.model.video;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoBookmarkId implements Serializable {
  private String userUuid;
  private Long videoId;
}
