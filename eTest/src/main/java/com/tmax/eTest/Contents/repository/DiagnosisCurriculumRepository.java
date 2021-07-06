package com.tmax.eTest.Contents.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tmax.eTest.Contents.model.DiagnosisCurriculum;

public interface DiagnosisCurriculumRepository extends JpaRepository<DiagnosisCurriculum, Long> {
    List<DiagnosisCurriculum> findByChapter(String chapter);
}
