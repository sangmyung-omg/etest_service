package com.tmax.eTest.Common.model.video;

import java.sql.Timestamp;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import lombok.Data;

@Data
@Entity
public class Video {
  @Id
  private Long videoId;
  private String videoSrc;
  private String originalFileSrc;
  private String title;
  private Timestamp createDate;
  private String creatorId;
  private String imgSrc;
  private Float totalTime;
  private Long curriculumId;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "curriculumId", insertable = false, updatable = false)
  private VideoCurriculum videoCurriculum;

  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "videoId")
  private VideoHit videoHit;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "ukMaster", cascade = CascadeType.ALL, orphanRemoval = true)
  // @JoinColumn(name = "videoId")
  private Set<VideoUkRel> videoUks = new LinkedHashSet<VideoUkRel>();

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "video", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<VideoBookmark> videoBookmarks = new LinkedHashSet<VideoBookmark>();
}
