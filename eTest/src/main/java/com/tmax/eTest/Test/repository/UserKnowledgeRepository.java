package com.tmax.eTest.Test.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.tmax.eTest.Common.model.user.UserKnowledge;
import org.springframework.stereotype.Repository;

@Repository("TE-UserKnowledgeRepository")

public interface UserKnowledgeRepository extends CrudRepository<UserKnowledge, String> {
	List<UserKnowledge> findByUserUuid(String userUuid);

}
