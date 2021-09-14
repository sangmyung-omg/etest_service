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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@IdClass(VideoBookmarkId.class)
public class VideoBookmark implements Persistable<VideoBookmarkId> {
  @Id
  private String userUuid;
  @Id
  private String videoId;

  public VideoBookmark(String userUuid, String videoId) {
    this.userUuid = userUuid;
    this.videoId = videoId;
  }

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "videoId", insertable = false, updatable = false, nullable = true)
  private Video video;

  @Transient
  private boolean isNew = true;

  @Transient
  private VideoBookmarkId videoBookmarkId;

  // @Transient
  // private CommonUtils commonUtils;

  @Override
  public VideoBookmarkId getId() {
    return new CommonUtils().objectNullcheck(videoBookmarkId)
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
