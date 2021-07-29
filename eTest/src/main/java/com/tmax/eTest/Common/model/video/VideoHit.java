package com.tmax.eTest.Common.model.video;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import lombok.Data;

@Data
@Entity
public class VideoHit {
  @Id
  private Long videoId;
  private Integer hit;

  @OneToOne
  @JoinColumn(name = "videoId")
  private Video video;
}
