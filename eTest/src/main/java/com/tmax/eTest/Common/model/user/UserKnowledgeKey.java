package com.tmax.eTest.Common.model.user;

import java.io.Serializable;

import lombok.Data;

@Data
public class UserKnowledgeKey implements Serializable {
	String userUuid;
	Integer ukId;
}
