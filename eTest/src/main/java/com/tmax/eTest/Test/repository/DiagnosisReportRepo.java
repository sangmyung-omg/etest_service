package com.tmax.eTest.Test.repository;

import org.springframework.data.repository.CrudRepository;

import com.tmax.eTest.Common.model.report.DiagnosisReport;

public interface DiagnosisReportRepo extends CrudRepository<DiagnosisReport, String> {

}
