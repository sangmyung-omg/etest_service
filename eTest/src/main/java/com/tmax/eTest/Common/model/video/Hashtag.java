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
public class Hashtag {
  @Id
  private Long hashtagId;
  private String name;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "hashtag", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<VideoHashtag> videoHashtags = new LinkedHashSet<VideoHashtag>();

}
