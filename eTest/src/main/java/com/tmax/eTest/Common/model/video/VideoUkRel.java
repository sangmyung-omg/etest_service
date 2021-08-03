package com.tmax.eTest.Common.model.video;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.tmax.eTest.Common.model.uk.UkMaster;

import lombok.Data;

@Data
@Entity
@IdClass(VideoUkRelId.class)
public class VideoUkRel {
  @Id
  private Long videoId;
  @Id
  private Long ukId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "ukId", insertable = false, updatable = false, nullable = true)
  private UkMaster ukMaster;
}
