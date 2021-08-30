package com.tmax.eTest.TestStudio.dto;

import com.tmax.eTest.Common.model.uk.UkMaster;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UKListDTO {
	private Integer id;
	private String name;
	
	public UKListDTO(UkMaster u) {
		this.id = u.getUkId();
		this.name = u.getUkName();
	}
}