package com.tmax.eTest.Common.model.video;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.Transient;

import com.tmax.eTest.Contents.util.CommonUtils;

import org.springframework.data.domain.Persistable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@IdClass(VideoBookmarkId.class)
public class VideoBookmark implements Persistable<VideoBookmarkId> {
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

  @Transient
  private boolean isNew = true;

  @Transient
  private VideoBookmarkId videoBookmarkId;

  @Override
  public VideoBookmarkId getId() {
    return CommonUtils.objectNullcheck(videoBookmarkId)
        ? videoBookmarkId = new VideoBookmarkId(this.userUuid, this.videoId)
        : videoBookmarkId;
  }

  @Override
  public boolean isNew() {
    return this.isNew;
  }

  @PrePersist
  @PostLoad
  public void markNotNew() {
    this.isNew = false;
  }
}