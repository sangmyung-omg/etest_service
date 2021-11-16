package com.tmax.eTest.Push.dto;

import lombok.Data;

@Data
public class CategoryPushRequestDTO {
    private String category;
    private String title;
    private String body;
    private String image;
}
