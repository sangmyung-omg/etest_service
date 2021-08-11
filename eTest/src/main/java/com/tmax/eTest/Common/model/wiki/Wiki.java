package com.tmax.eTest.Common.model.wiki;

import java.sql.Timestamp;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Wiki {
  @Id
  private Long wikiId;
  private String title;
  private Timestamp createDate;
  private String creatorId;
  private String description;
  private String summary;
  private String source;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "wiki", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<WikiUkRel> wikiUks = new LinkedHashSet<WikiUkRel>();

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "wiki", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<WikiBookmark> wikiBookmarks = new LinkedHashSet<WikiBookmark>();
}
