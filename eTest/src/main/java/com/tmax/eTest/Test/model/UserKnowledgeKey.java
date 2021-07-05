package com.tmax.eTest.Test.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class UserKnowledgeKey implements Serializable {
	String userUuid;
	Integer ukId;
}
