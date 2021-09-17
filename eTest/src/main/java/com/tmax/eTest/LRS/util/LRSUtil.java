package com.tmax.eTest.LRS.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class LRSUtil {

	public static Timestamp timeStringToTimestampObj(String timestampStr)
	{
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.KOREA);
		Timestamp timestampObj = null;
		
		try {
			timestampObj = new Timestamp(dateFormat.parse(timestampStr).getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return timestampObj;
	}
	
}
