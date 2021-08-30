package com.tmax.eTest.TestStudio.dto;

import com.tmax.eTest.Common.model.problem.Part;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PartListDTO {
	private Integer id;
	private String name;
	private Integer order;
	private Integer count;
	
	public PartListDTO(Part p) {
		this.id = p.getPartID();
		this.name = p.getPartName();
		this.order = p.getOrderNum();
		this.count = p.getProblemCount();
	}
}