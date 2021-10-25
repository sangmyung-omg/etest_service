package com.tmax.eTest.Common.model.report;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportCacheKey implements Serializable {
        String reportId;
        String userId;
}
