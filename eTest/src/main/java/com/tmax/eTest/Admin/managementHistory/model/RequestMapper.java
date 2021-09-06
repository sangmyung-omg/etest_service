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

    @Column(name = "method")
    private String method;

    @Column(name = "controller")
    private String controller;

    @Column(name = "name")
    private String name;

    @Column(name = "menu")
    private String menu;

    @Column(name = "content")
    private String content;
}
