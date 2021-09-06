package com.tmax.eTest.Admin.managementHistory.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "management_history")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ManagementHistory {
    @Id
    @Column(name = "id")
    @SequenceGenerator(name = "MANAGEMENT_HISTORY_SEQ_GENERATOR", sequenceName = "MANAGEMENT_HISTORY_SEQ",
            allocationSize = 1, initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MANAGEMENT_HISTORY_SEQ_GENERATOR")
    private Long id;

    @Column(name = "admin_name")
    private String adminName;

    @Column(name = "admin_id")
    private String adminId;

    @Column(name = "ip")
    private String ip;

    @Column(name = "menu")
    private String menu;

    @Column(name = "detail")
    private String detail;

    @Column(name = "link")
    private String link;

    @Column(name = "reason")
    private String reason;

    @Column(name = "task_date")
    private Timestamp taskDate;
}