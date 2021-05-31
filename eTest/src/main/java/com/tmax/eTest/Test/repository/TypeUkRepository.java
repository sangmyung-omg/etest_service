package com.tmax.eTest.Test.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.tmax.eTest.Test.model.TypeUkMaster;

public interface TypeUkRepository extends CrudRepository<TypeUkMaster, String> {
	@Query("SELECT T.typeUkUuid FROM TypeUkMaster T WHERE SUBSTR(T.curriculumId, 1, 11) in :sectionList")
	List<String> findByCurriculum(@Param("sectionList") List<String> sectionList);
}
