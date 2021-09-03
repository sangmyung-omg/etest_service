package com.tmax.eTest.Admin.dashboard.dto;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class SignUpCreateDateDTO {
    private LocalDateTime dateFrom;
    private LocalDateTime dateTo;
}
