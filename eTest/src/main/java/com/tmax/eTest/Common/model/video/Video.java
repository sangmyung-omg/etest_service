package com.tmax.eTest.Common.model.video;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
  private String subject;
  private Float totalTime;

  @OneToOne
  @JoinColumn(name = "videoId")
  private VideoHit hit;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "video")
  private List<VideoUkRel> videoUks = new ArrayList<VideoUkRel>();

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "video")
  private List<VideoBookmark> videoBookmarks = new ArrayList<VideoBookmark>();

}
