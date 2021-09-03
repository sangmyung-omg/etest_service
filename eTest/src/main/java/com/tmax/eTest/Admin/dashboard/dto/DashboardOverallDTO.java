package com.tmax.eTest.Admin.dashboard.dto;

import lombok.*;

@Getter
@Setter
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardOverallDTO {
    private int totalAccessUser;
    private int userIncrease;
    private int userRegistered;
    private int userDeleted;
    private int userTotal;
    private int diagnosisTotal;
    private int diagnosis;
    private int minitest;
}
