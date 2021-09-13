package com.tmax.eTest.Test.dto;

import java.util.List;

import lombok.Data;

@Data
public class UkRelOutputDTO {
    private String resultMessage;
    private List<Integer> baseUk;
    private List<Integer> preUk;
}
