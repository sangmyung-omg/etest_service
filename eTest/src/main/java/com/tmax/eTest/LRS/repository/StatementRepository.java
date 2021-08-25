package com.tmax.eTest.LRS.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import com.tmax.eTest.LRS.model.Statement;

public interface StatementRepository extends CrudRepository<Statement, String>, JpaSpecificationExecutor<Statement> 
{

	/*
    @Query("SELECT DISTINCT CM.chapter FROM CurriculumDAO CM WHERE CM.curriculumId LIKE ?1")
	List<String> findAllByCurriculumIdLike(String curriculumId);

	@Query("SELECT CM FROM CurriculumDAO CM WHERE CM.grade = ?1 AND CM.chapter = ?2 AND CHAR_LENGTH(CM.curriculumId)=11")
	CurriculumDAO findByChapter(String grade, String chapter);

	@Query(value="SELECT * "
			+ "FROM "
				+ "curriculum_master a, " 
				+ "(select curriculum_sequence from curriculum_master where curriculum_id=:chapter) b " 
			+ "where "
				+ "CHAR_LENGTH(a.curriculum_id)=14 "
			+ "and "
				+ "a.curriculum_sequence < b.curriculum_sequence "
			+ "order by "
				+ "a.curriculum_sequence "
			+ "DESC limit 1", nativeQuery=true)
	CurriculumDAO findNearestSection(@Param("chapter") String chapter);
	
	@Query(value="select section from curriculum_master where curriculum_id=?1", nativeQuery=true)
	String findSectionNameById(String curriculum_id);
    */
}
