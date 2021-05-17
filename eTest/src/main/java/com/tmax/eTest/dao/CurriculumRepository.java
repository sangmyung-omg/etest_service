package com.tmax.eTest.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface CurriculumRepository extends CrudRepository<CurriculumDAO, String> {
	@Query("SELECT DISTINCT CM.chapter FROM CurriculumDAO CM WHERE CM.curriculumId LIKE ?1")
	List<String> findAllByCurriculumIdLike(String curriculumId);
	
//	@Query("SELECT CM FROM CurriculumDAO CM WHERE CM.grade = ?1 AND CM.chapter = ?2 AND CHAR_LENGTH(CM.curriculumId)=11")
	@Query("SELECT CM FROM CurriculumDAO CM WHERE CM.grade = ?1 AND CM.chapter = ?2 AND LENGTH(CM.curriculumId)=11")
	CurriculumDAO findByChapter(String grade, String chapter);
}