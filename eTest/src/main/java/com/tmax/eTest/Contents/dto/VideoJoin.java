package com.tmax.eTest.Contents.dto;

import com.tmax.eTest.Common.model.video.Video;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VideoJoin {
  private Video video;
  private String userUuid;
}
