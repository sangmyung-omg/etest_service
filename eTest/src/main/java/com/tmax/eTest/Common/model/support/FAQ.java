package com.tmax.eTest.Common.model.support;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor

public class FAQ {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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
