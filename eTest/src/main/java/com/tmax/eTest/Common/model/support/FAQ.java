package com.tmax.eTest.Common.model.support;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CS_FAQ")
public class FAQ {
    @Id
    @SequenceGenerator(name = "FAQ_SEQ_GENERATOR", sequenceName = "FAQ_SEQUENCE", allocationSize = 1, initialValue = 20000)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FAQ_SEQ_GENERATOR")
    @Column(name = "FAQ_ID")
    private Long id;

    @Column(name = "category")
    private String category;

    @Column(name = "draft")
    private int draft;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "CONTENT")
    private String content;

    @Column(name = "views")
    private Long views;

    @Column(name = "date_add")
    private Timestamp dateAdd;

    @Column(name = "date_edit")
    private Timestamp dateEdit;
}
