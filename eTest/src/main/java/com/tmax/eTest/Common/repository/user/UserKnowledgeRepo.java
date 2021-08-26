package com.tmax.eTest.Common.repository.user;

import java.util.List;

import com.tmax.eTest.Common.model.user.UserKnowledge;
import com.tmax.eTest.Common.model.user.UserKnowledgeKey;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserKnowledgeRepo extends JpaRepository<UserKnowledge, UserKnowledgeKey> {

	@Query("select uk from com.tmax.eTest.Common.model.user.UserKnowledge uk where uk.userUuid = :userUuid order by uk.ukMastery")
	List<UserKnowledge> findUKListByUserUuid(@Param("userUuid") String userUuid);
}
