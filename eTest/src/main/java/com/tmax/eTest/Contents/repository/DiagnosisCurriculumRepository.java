package com.tmax.eTest.Contents.repository;

import java.util.List;

import com.tmax.eTest.Common.model.problem.DiagnosisCurriculum;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("CO-DiagnosisCurriculumRepository")
public interface DiagnosisCurriculumRepository extends JpaRepository<DiagnosisCurriculum, Long> {
    List<DiagnosisCurriculum> findByChapterOrderByCurriculumIdAsc(String chapter);
}
