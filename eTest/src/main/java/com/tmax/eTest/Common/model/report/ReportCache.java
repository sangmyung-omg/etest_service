package com.tmax.eTest.Common.model.report;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Lob;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@IdClass(ReportCacheKey.class)
@Table(name="REPORT_CACHE")
public class ReportCache {
    @Id 
    private String reportId;
    @Id
    private String userId;

    private String type;

    @Lob
    private String data;
    
    private Timestamp updateTime;
}
