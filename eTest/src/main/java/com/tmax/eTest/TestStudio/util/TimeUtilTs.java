package com.tmax.eTest.TestStudio.util;

import java.io.File;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Component
@Getter
public class TimeUtilTs {

	
	public static ZonedDateTime DateToZonedDT_System(Date input) {
		
		if(input==null) return null;
		
		return input.toInstant().atZone(ZoneId.systemDefault());
		
	}
	
	public static ZonedDateTime DateToZonedDT_Seoul(Date input) {
		
		if(input==null) return null;
		
		return input.toInstant().atZone(ZoneId.of("Asia/Seoul"));
		
	}
	
}
