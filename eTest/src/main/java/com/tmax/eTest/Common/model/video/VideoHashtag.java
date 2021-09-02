package com.tmax.eTest.Common.model.video;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@IdClass(VideoHashtagId.class)
public class VideoHashtag {
  @Id
  private String videoId;
  @Id
  private Long hashtagId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "videoId", insertable = false, updatable = false, nullable = true)
  private Video video;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "hashtagId", insertable = false, updatable = false, nullable = true)
  private Hashtag hashtag;

}