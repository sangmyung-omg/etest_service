package com.tmax.eTest.Common.model.video;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class VideoHit {
  @Id
  private Long videoId;
  private Integer hit;
}
