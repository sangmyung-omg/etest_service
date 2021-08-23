package com.tmax.eTest.Common.model.support;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor

@Table(name = "CS_FAQ")
public class FAQ {
    @Id
    @SequenceGenerator(name = "FAQ_SEQ_GENERATOR", sequenceName = "FAQ_SEQUENCE", allocationSize = 1, initialValue = 20000)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FAQ_SEQ_GENERATOR")
    @Column(name = "FAQ_ID")
    private Long id;

    @Column(name = "FAQ_TITLE")
    private String title;

    @Column(name = "FAQ_CONTENT")
    private String content;

    public FAQ(String title, String content) {
        this.title = title;
        this.content = content;
    }

}
