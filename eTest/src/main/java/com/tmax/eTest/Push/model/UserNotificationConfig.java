package com.tmax.eTest.Push.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "USER_NOTIFICATION_CONFIG")
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserNotificationConfig {
    @Id
    @Column(name = "TOKEN")
    private String token;

    @Column(name = "USER_UUID")
    private String userUuid;

    @Column(name = "GLOBAL")
    private String global;

    @Column(name = "NOTICE")
    private String notice;

    @Column(name = "INQUIRY")
    private String inquiry;

    @Column(name = "CONTENT")
    private String content;
}
