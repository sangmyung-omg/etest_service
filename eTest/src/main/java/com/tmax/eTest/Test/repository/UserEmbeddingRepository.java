package com.tmax.eTest.Test.repository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.tmax.eTest.Common.model.user.UserEmbedding;
import org.springframework.stereotype.Repository;

@Repository("TE-UserEmbeddingRepository")

public interface UserEmbeddingRepository extends CrudRepository<UserEmbedding, String> {
//	@Query("SELECT UE.userEmbedding FROM #{#entityName} UE WHERE UE.userUuid LIKE ?1")
//	CurriculumDAO findByUserUuid(String userUuid);

}
