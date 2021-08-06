package com.tmax.eTest.Common.model.support;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.models.auth.In;
import lombok.*;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor


@Builder
@Table(name = "CS_INQUIRY_FILE")
public class Inquiry_file {
    @Id
    @SequenceGenerator(name = "INQUIRY_FILE_SEQ_GENERATOR", sequenceName = "INQUIRY_FILE_SEQUENCE", allocationSize = 1, initialValue = 20000)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "INQUIRY_FILE_SEQ_GENERATOR")

    @Column(name = "INQUIRY_FILE_ID")
    private Long id;

    @Column(name = "INQUIRY_FILE_URL")
    private String url;

    @Column(name = "INQUIRY_FILE_TYPE")
    private String type;

    @ManyToOne
    @JoinColumn(name = "INQUIRY_ID")
    @JsonIgnoreProperties({"inquiry_file"})

    private Inquiry inquiry;

    public Inquiry_file(@JsonProperty("url") String url,
                        @JsonProperty("type") String type,
                        @JsonProperty("inquiry") Inquiry inquiry) {
    this.url = url;
    this.type = type;
    this. inquiry = inquiry;
    }

}