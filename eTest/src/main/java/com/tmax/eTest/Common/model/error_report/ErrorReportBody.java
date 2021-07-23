package com.tmax.eTest.Common.model.error_report;

import lombok.Data;

@Data
public class ErrorReportBody {
	private long problem_id;
	private String id;
	private String report_type;
	private String report_text;
}
