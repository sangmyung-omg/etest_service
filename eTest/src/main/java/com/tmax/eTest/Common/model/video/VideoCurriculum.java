package com.tmax.eTest.Common.model.video;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class VideoCurriculum {
  @Id
  private Long curriculumId;
  private String subject;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "videoCurriculum", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<Video> videos = new LinkedHashSet<Video>();
}
