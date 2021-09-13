package com.tmax.eTest.Test.service;

import com.tmax.eTest.Test.dto.UkRelOutputDTO;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("ModelUpdateService")
@Primary
public class ModelUpdateService {
    
    public UkRelOutputDTO getUkRelInfo() {
        UkRelOutputDTO output = new UkRelOutputDTO();

        return output;
    }
}
