package com.tmax.eTest.Common.model.support;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@Builder
@Table(name = "CS_INQUIRY")
public class Inquiry {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "INQUIRY_ID")
    private Long id;

    @Column(name="INQUIRY_STATUS")
    private String status;

    @Column(name = "INQUIRY_TITLE")
    private String title;

    @Column(name = "INQUIRY_TYPE")
    private String type;

    @Column(name = "INQUIRY_DATE")
    private LocalDate date;

    @Column(name = "INQUIRY_CONTENT")
    private String content;

    @Column(name = "INQUIRY_URL")
    private String URL;

    @Column(name = "INQUIRY_ANSWER")
    private String answer;

}


