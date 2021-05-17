package com.tmax.eTest.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface TypeUkRepository extends CrudRepository<TypeUkDAO, String> {
	@Query("SELECT T.typeUkUuid FROM TypeUkDAO T WHERE SUBSTR(T.curriculumId, 1, 11) in :sectionList")
	List<String> findByCurriculum(@Param("sectionList") List<String> sectionList);
}
