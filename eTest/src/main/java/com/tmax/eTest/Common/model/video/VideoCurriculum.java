package com.tmax.eTest.Common.model.video;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class VideoCurriculum {
  @Id
  private Long curriculumId;
  private String subject;
}
