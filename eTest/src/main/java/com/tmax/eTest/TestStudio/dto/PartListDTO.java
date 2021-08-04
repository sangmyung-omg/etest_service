package com.tmax.eTest.TestStudio.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PartListDTO {
	private Integer id;
	private String name;
	private Integer order;
	private Integer count;
}