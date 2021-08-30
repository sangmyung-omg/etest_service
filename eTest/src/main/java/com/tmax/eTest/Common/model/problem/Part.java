package com.tmax.eTest.Common.model.problem;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name="PART")
@NoArgsConstructor
@SequenceGenerator(
		  name = "PART_SEQ_GENERATOR", 
		  sequenceName = "PART_SEQ",
		  initialValue = 1,
		  allocationSize = 1)
public class Part {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator = "PART_SEQ_GENERATOR")
	@Column(name="PART_ID")
	private Integer partID;
	
	@Column(name="PART_NAME", length=100)
	private String partName;
	
	@Column(name="ORDER_NUM")
	private Integer orderNum;
	
	@Column(name="PROBLEM_COUNT")
	private Integer problemCount;
	
	public Part(String partName, Integer orderNum, Integer problemCount) {
		this.partName = partName;
		this.orderNum = orderNum;
		this.problemCount = problemCount;
	}
	
	public void updatePart(String partName, Integer orderNum, Integer problemCount) {
		this.partName = partName;
		this.orderNum = orderNum;
		this.problemCount = problemCount;
	}
}