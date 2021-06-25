package com.tmax.eTest.Report.dto.triton;

import java.util.List;
import java.util.Map;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TritonInferInputDTO {
	String id;
	
	List<Map<String, Object>> inputs;
	List<Map<String, String>> outputs;
	
}
