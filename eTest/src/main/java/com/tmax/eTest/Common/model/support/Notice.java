package com.tmax.eTest.Common.model.support;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "CS_NOTICE")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notice {
    @Id
    @Column(name = "NOTICE_ID")
    @SequenceGenerator(name = "NOTICE_SEQ_GENERATOR", sequenceName = "NOTICE_SEQUENCE", allocationSize = 1, initialValue = 20000)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NOTICE_SEQ_GENERATOR")
    private Long id;

    @Column(name = "draft")
    private int draft;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "views")
    private Long views;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = " image_encoding")
    private String imageEncoding;

    @Column(name = "date_add")
    private Timestamp dateAdd;

    @Column(name = "date_edit")
    private Timestamp dateEdit;
}
