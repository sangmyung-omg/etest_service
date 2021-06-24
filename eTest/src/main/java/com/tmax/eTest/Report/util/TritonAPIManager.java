package com.tmax.eTest.Report.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Call StatementList GET API from LRS Server
 * 
 * @author sangheonLee
 */
@Component
public class TritonAPIManager {

	private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

	private final String HOST = "http://192.168.153.132:8080";
//	private static final String HOST = System.getenv("LRS_HOST");

}
