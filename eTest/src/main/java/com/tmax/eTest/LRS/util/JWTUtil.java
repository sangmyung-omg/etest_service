package com.tmax.eTest.LRS.util;

import java.util.Base64;
import java.util.regex.PatternSyntaxException;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JWTUtil {
    static public String getJWTPayloadField(String token, String fieldName) {
        //Split jwt
    	String result = null;
    	try
    	{
	        String[] chunks = token.split("\\.");
	
	        Base64.Decoder decoder = Base64.getUrlDecoder();
	
	        String payload = null;
	        
	        if(chunks.length > 1)
	        	payload = new String(decoder.decode(chunks[1]));
	        else
	        	return result;
	
	        JsonObject jsonObj = JsonParser.parseString(payload).getAsJsonObject();
	        
	        result =  jsonObj.get(fieldName).getAsString();
    	}
    	catch(PatternSyntaxException e)
    	{
    		log.info("JWT Token Split Fail - "+ token+ " "+fieldName);
    	}
    	catch(IllegalArgumentException e)
    	{
    		log.info("JWT Token Decode Fail or getJsonObject Fail - "+ token+ " "+fieldName);
    	}
    	catch(JsonParseException e)
    	{
    		log.info("JWT Token Json Parse Fail - "+ token+ " "+fieldName);
    	}
    	catch(ClassCastException e)
    	{
    		log.info("JWT Token get string in jsonObject Fail - "+ token+ " "+fieldName);
    	}
    	
		return result;
    }
    
}
