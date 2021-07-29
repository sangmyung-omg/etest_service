package com.tmax.eTest.Common.model.video;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

import lombok.Data;

@Data
@Entity
@IdClass(VideoUkRelId.class)
public class VideoUkRel {
  @Id
  private Long videoId;
  @Id
  private Long ukId;
}
