package com.tmax.eTest.Test.controller;

import com.tmax.eTest.Test.config.TestPathConstant;
import com.tmax.eTest.Test.dto.UkRelOutputDTO;
import com.tmax.eTest.Test.service.ModelUpdateService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping(path = "/model")
public class ModelUpdateController {
    
    @Autowired
    ModelUpdateService modelService;

    @GetMapping(value = "/ukRel", produces = "application/json; charset=utf-8")
    public ResponseEntity<Object> getUkRelations() {
        log.info("> UK Rel logic start!");
        UkRelOutputDTO result = modelService.getUkRelInfo();
        if (result.getResultMessage().contains("error") | result.getResultMessage().contains("warning")) {
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
