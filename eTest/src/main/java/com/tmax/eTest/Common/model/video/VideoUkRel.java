package com.tmax.eTest.Common.model.video;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.tmax.eTest.Common.model.uk.UkMaster;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@IdClass(VideoUkRelId.class)
public class VideoUkRel {
  @Id
  private String videoId;
  @Id
  private Long ukId;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "videoId", insertable = false, updatable = false, nullable = true)
  private Video video;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "ukId", insertable = false, updatable = false, nullable = true)
  private UkMaster ukMaster;
}
