package com.tmax.eTest.TestStudio.util;

import java.io.File;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Component
public class StringUtilTs {
	
    public final static String UTF8_BOM = "\uFEFF";
    
    public static String removeUTF8BOMStart(String input) {
        if( input.startsWith(UTF8_BOM) ) {
            input = input.substring(1);
        }
        return input;
    }
    
    public static String removeUTF8BOMInString(String input) {
        
    	input = input.replaceAll(UTF8_BOM, "");
    	
        return input;
    }

}
