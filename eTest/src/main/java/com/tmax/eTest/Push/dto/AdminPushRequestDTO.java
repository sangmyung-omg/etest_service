package com.tmax.eTest.Push.dto;

import lombok.Data;

import java.util.List;

@Data
public class AdminPushRequestDTO {
    private List<String> token;
    private String category;
    private String title;
    private String body;
    private String image;
}
