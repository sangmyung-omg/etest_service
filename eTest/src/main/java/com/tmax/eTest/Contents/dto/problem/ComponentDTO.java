package com.tmax.eTest.Contents.dto.problem;

import java.util.List;

import lombok.Data;

@Data
public class ComponentDTO {
    private List<String> data;
    private String type;
    private String preface;
}
