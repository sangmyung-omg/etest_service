package com.tmax.eTest.Push.dto;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class UserNotificationConfigEditDTO {
    private String userUuid;
    private String category;
    private String value;
}
