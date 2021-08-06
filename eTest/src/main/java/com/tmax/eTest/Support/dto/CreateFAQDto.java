package com.tmax.eTest.Support.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateFAQDto {
    private String title;

    private String content;
}
