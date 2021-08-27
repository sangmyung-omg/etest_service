package com.tmax.eTest.TestStudio.dto.problems.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseDiagCurriculumandProblemDTO {

	private BaseProblemDTO problem;
	private BaseDiagCurriculumDTO diagCurriculum;

}
