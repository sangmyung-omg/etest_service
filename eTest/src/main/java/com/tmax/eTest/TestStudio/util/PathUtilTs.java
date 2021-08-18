package com.tmax.eTest.TestStudio.util;

import java.io.File;

import org.springframework.stereotype.Component;

import lombok.Getter;

@Component
@Getter
public class PathUtilTs {
	
	private String dirPath = File.separator + "data" + File.separator + "imgsrc";
	private String statusOn = "출제";
	public static String statusOn_ = "출제";
	private String statusOff = "보류";
	public static String statusOff_ = "보류";
	
}
