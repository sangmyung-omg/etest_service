package com.tmax.eTest.Report.dto.triton;

import java.util.List;
import java.util.Map;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TritonInferOutputDTO {
	String id;
	String model_name;
	String model_version;
	
	List<Map<String, String>> outputs;
	
}
