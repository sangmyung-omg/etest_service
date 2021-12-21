package com.tmax.eTest.Common.model.event;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

import com.tmax.eTest.Event.dto.Channel;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Event {
  @Id
  private String userUuid;
  @Column(unique = true)
  private String phoneNumber;
  @Enumerated(EnumType.STRING)
  private Channel channel;
  @CreatedDate
  private LocalDateTime createDate;
  private Integer step;
  @Builder.Default
  private String privacyPolicyAgree = "0";
  @Builder.Default
  private String thirdPartyAgree = "0";
}
