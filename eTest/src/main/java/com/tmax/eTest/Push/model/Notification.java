package com.tmax.eTest.Push.model;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "NOTIFICATION")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    @Id
    @SequenceGenerator(name = "NOTIFICATION_SEQ_GENERATOR", sequenceName = "NOTIFICATION_SEQ",
            allocationSize = 1, initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NOTIFICATION_SEQ_GENERATOR")
    @Column(name = "ID")
    private Long id;

    @Column(name = "USER_UUID")
    private String userUuid;

    @Column(name = "TOKEN")
    private String token;

    @Column(name = "READ")
    private String read;

    @Column(name = "CATEGORY")
    private String category;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "TIMESTAMP")
    private Timestamp timestamp;
}
