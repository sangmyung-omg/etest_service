package com.tmax.eTest.Contents.repository;

import com.tmax.eTest.Common.model.error_report.ErrorReport;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ErrorReportRepository extends JpaRepository<ErrorReport, Long>{

}
