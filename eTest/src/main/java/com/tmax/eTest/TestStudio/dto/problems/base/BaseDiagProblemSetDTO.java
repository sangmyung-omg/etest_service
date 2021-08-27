package com.tmax.eTest.TestStudio.dto.problems.base;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseDiagProblemSetDTO {

	private BaseProblemDTO problem;
	private List<BaseProbChoiceDTO> probChoices;
	private List<BaseProbUKRelDTO> probUKRels;
	private BaseDiagCurriculumDTO diagCurriculum;

}
