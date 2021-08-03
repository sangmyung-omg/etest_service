package com.tmax.eTest.Common.model.video;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

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

  @ManyToOne
  @JoinColumn(name = "curriculumId", insertable = false, updatable = false)
  private VideoCurriculum videoCurriculum;

  @OneToOne
  @JoinColumn(name = "videoId")
  private VideoHit videoHit;

  @OneToMany(fetch = FetchType.LAZY)
  @JoinColumn(name = "videoId")
  private List<VideoUkRel> videoUks = new ArrayList<VideoUkRel>();

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "video", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<VideoBookmark> videoBookmarks = new ArrayList<VideoBookmark>();

}
