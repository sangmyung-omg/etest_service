package com.tmax.eTest.Common.model.video;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@IdClass(VideoBookmarkId.class)
public class VideoBookmark {
  @Id
  private String userUuid;
  @Id
  private Long videoId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "videoId", insertable = false, updatable = false, nullable = true)
  private Video video;

  public VideoBookmark(String userUuid, Long videoId) {
    this.userUuid = userUuid;
    this.videoId = videoId;
  }
}
