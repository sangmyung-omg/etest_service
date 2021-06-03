package com.tmax.eTest.Test.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="TYPE_UK_MASTER")
public class TypeUkMaster {
	@Id
	private String typeUkUuid;
	
	private String typeUkName;
	private String typeUkDescription;
	private String curriculumId;
}
