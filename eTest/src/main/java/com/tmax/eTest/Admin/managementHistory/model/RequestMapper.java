package com.tmax.eTest.Admin.managementHistory.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "request_mapper")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestMapper {
    @Id
    @Column(name = "id")
    @SequenceGenerator(name = "REQUEST_MAPPER_SEQ_GENERATOR", sequenceName = "REQUEST_MAPPER_SEQ",
            allocationSize = 1, initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REQUEST_MAPPER_SEQ_GENERATOR")
    private Long id;

    @Column(name = "post_method")
    private String postMethod;

    @Column(name = "request_url")
    private String requestUrl;

    @Column(name = "parameter")
    private String parameter;

    @Column(name = "menu")
    private String menu;

    @Column(name = "content")
    private String content;
}
