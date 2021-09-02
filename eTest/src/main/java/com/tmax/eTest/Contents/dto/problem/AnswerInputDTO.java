package com.tmax.eTest.Contents.dto.problem;

import java.util.List;

import com.tmax.eTest.LRS.dto.StatementDTO;

import lombok.Data;

@Data
public class AnswerInputDTO {
    private List<StatementDTO> lrsbody;
    private String nrUuid;
}
