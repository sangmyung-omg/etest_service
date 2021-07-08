package com.tmax.eTest.Test.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.tmax.eTest.Test.model.UserKnowledge;
import com.tmax.eTest.Test.model.UserKnowledgeKey;

public interface UserKnowledgeRepository extends CrudRepository<UserKnowledge, UserKnowledgeKey> {
	

}
