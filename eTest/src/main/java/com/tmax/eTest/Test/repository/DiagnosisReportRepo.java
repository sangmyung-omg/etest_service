package com.tmax.eTest.Test.repository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

import com.tmax.eTest.Common.model.report.DiagnosisReport;
import org.springframework.stereotype.Repository;

@Repository("TE-DiagnosisReportRepo")
public interface DiagnosisReportRepo extends CrudRepository<DiagnosisReport, String> {

    List<DiagnosisReport> findByUserUuid(String userUuid) throws Exception;
}
