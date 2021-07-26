package com.tmax.eTest.Test.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.tmax.eTest.Common.model.user.UserKnowledge;
import com.tmax.eTest.Common.model.user.UserKnowledgeKey;

public interface UserKnowledgeRepository extends CrudRepository<UserKnowledge, UserKnowledgeKey> {
	

}
