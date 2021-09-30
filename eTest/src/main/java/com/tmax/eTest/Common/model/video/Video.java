package com.tmax.eTest.Common.model.video;

import java.sql.Date;
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

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Video {
  @Id
  private String videoId;
  private String videoSrc;
  private String originalFileSrc;
  private String title;
  private String imgSrc;
  private Long curriculumId;
  private Float totalTime;
  private Float startTime;
  private Float endTime;
  private Date createDate;
  private Date registerDate;
  private Date endDate;
  private Long sequence;
  private String codeSet;
  private String type;
  private String related;
  private String description;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "curriculumId", insertable = false, updatable = false, nullable = true)
  private VideoCurriculum videoCurriculum;

  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "videoId")
  private VideoHit videoHit;

  @OneToMany(fetch = FetchType.EAGER, mappedBy = "video", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<VideoBookmark> videoBookmarks = new LinkedHashSet<VideoBookmark>();

  @OneToMany(fetch = FetchType.EAGER, mappedBy = "video", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<VideoUkRel> videoUks = new LinkedHashSet<VideoUkRel>();

  @OneToMany(fetch = FetchType.EAGER, mappedBy = "video", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<VideoHashtag> videoHashtags = new LinkedHashSet<VideoHashtag>();
}
