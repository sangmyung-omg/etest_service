package com.tmax.eTest.TestStudio.util;

import java.io.File;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Component
@Getter
public class PathUtilTs {
//	@Value("${etest.contents.file.defaultPath}")
	@Value("${file.path}")
	private String defaultPath;
//	private String dirPath = defaultPath +"problem" + File.separator + "imgsrc";
	private String statusOn = "출제";
	public static String statusOn_ = "출제";
	private String statusOff = "보류";
	public static String statusOff_ = "보류";
	
	public String getDirPath() {
		
		String dirPath = defaultPath +"problem" + File.separator + "imgsrc";
		
		return dirPath;
	}

}
