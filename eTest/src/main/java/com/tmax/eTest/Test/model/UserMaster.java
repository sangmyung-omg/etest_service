package com.tmax.eTest.Test.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="USER_MASTER")
public class UserMaster {
	@Id
	private String userUuid;
	private String name;
	private String userType;
}
