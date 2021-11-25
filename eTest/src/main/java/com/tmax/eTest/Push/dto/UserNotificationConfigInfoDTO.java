package com.tmax.eTest.Push.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserNotificationConfigInfoDTO {
    private String global;
    private String notice;
    private String inquiry;
    private String content;
}
