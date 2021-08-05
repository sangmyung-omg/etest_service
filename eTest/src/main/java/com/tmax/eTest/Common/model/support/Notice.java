package com.tmax.eTest.Common.model.support;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "CS_NOTICE")
@NoArgsConstructor
public class Notice {
    @Id
    @Column(name = "NOTICE_ID")
    @SequenceGenerator(name = "NOTICE_SEQ_GENERATOR", sequenceName = "NOTICE_SEQUENCE", allocationSize = 1, initialValue = 20000)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NOTICE_SEQ_GENERATOR")
    private Long id;

    @Column(name = "NOTICE_TITLE")
    private String title;

    @Column(name = "NOTICE_DATE")
    private LocalDate date;

    @Column(name = "NOTICE_CONTENT")
    private String content;

    public Notice(String title, LocalDate date, String content){
        this.title = title;
        this.date = date;
        this.content = content;
    }
}
