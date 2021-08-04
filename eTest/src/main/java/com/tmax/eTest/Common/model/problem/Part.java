package com.tmax.eTest.Common.model.problem;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="PART")
public class Part {
	@Id
	@Column(name="PART_ID")
	private Integer partID;
	
	@Column(name="PART_NAME", length=100)
	private String partName;
	
	@Column(name="ORDER_NUM")
	private Integer orderNum;
	
	@Column(name="PROBLEM_COUNT")
	private Integer problemCount;
}
