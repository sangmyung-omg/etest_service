package com.tmax.eTest.Event.dto;

import java.time.LocalDateTime;

import com.tmax.eTest.Common.model.event.Event;

import org.springframework.beans.BeanUtils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventDTO {
  private String phoneNumber;
  private Channel channel;
  private LocalDateTime createDate;
  private Integer step;
  private String privacyPolicyAgree;
  private String thirdPartyAgree;

  public EventDTO(Event event) {
    BeanUtils.copyProperties(event, this);
  }
}
