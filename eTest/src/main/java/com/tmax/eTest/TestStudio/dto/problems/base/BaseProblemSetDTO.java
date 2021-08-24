package com.tmax.eTest.TestStudio.dto.problems.base;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.tmax.eTest.TestStudio.dto.problems.base.BaseDiagCurriculumDTO;
import com.tmax.eTest.TestStudio.dto.problems.base.BaseProblemDTO;
import com.tmax.eTest.TestStudio.dto.problems.base.BaseTestProblemDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseProblemSetDTO {
	
	private BaseProblemDTO problem;
	private List<BaseProbChoiceDTO> probChoices;
	private List<BaseProbUKRelDTO> probUKRels;
	
}
