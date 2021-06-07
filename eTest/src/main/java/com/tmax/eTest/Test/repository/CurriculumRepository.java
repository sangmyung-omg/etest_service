package com.tmax.eTest.Test.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.tmax.eTest.Test.model.CurriculumMaster;

public interface CurriculumRepository extends CrudRepository<CurriculumMaster, String> {
	@Query("SELECT DISTINCT CM.chapter FROM CurriculumMaster CM WHERE CM.curriculumId LIKE ?1")
	List<String> findAllByCurriculumIdLike(String curriculumId);
	
//	@Query("SELECT CM FROM CurriculumDAO CM WHERE CM.grade = ?1 AND CM.chapter = ?2 AND CHAR_LENGTH(CM.curriculumId)=11")
	@Query("SELECT CM FROM CurriculumMaster CM WHERE CM.grade = ?1 AND CM.chapter = ?2 AND LENGTH(CM.curriculumId)=11")
	CurriculumMaster findByChapter(String grade, String chapter);
}