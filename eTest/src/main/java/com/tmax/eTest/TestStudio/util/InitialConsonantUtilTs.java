package com.tmax.eTest.TestStudio.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;

import com.tmax.eTest.TestStudio.controller.component.exception.CustomExceptionTs;
import com.tmax.eTest.TestStudio.controller.component.exception.ErrorCodeEnumTs;

import lombok.Getter;

@Component
public class InitialConsonantUtilTs {
	
	public String InitialConsonantsV2( String jsonStr ) throws IOException, ParseException{
		
		     if(jsonStr.length()>2500) {
		    	 throw new CustomExceptionTs(ErrorCodeEnumTs.EXCEEDED_REQUEST_SIZE);
		     }
			
			 String rtName = "";
			
			 JSONParser jsonParser = new JSONParser();

			 JSONArray jsonArray = (JSONArray) jsonParser.parse(jsonStr);

			 for(int i=0; i< jsonArray.size() ; i++) {
				 
				 JSONObject jsonObj = (JSONObject) jsonArray.get(i);
				 if( jsonObj.containsKey("data") ) {
					 if( jsonObj.get("data").getClass().getName() == "java.lang.String" ) {
						 String tempData = (String) jsonObj.get("data");
//						 tempData = tempData.replaceAll( System.getProperty("line.separator").toString(), " ");
						 tempData = tempData.replaceAll("(\r\n|\r|\n|\n\r)", " ");
						 
						 rtName = rtName + InitialConsonants( tempData );
						 rtName = rtName + " ";
						 
					 }else if( jsonObj.get("data").getClass().getName() == "org.json.simple.JSONArray" ) {
//						 ArrayList<String> tempArrayList = (ArrayList<String>) jsonObj.get("data");
						 ArrayList<Object> tempArrayList = (ArrayList<Object>) jsonObj.get("data");
						 for(Object tempObjData : tempArrayList) {
							 
//							 tempData = tempData.replaceAll( System.getProperty("line.separator").toString(), " ");
							 String tempStrData = tempObjData.toString().replaceAll("(\r\n|\r|\n|\n\r)", " ");
							 
							 rtName = rtName + InitialConsonants( tempStrData );
							 rtName = rtName + " ";
							
						 }
					 }
				 }
				 
			 }
			 
			 return rtName;
			
	}
	
	/**
	 * 
	 * 	
	 */
	public String InitialConsonants( String name ) throws IOException{
		
    	String rtName = "";

    	char epName;


    		for (int i=0; i<name.length()&&i<2500; i++){

    			epName = name.charAt(i);
    			
    			if( Convert(epName)==" " ) {
    				if(rtName.length()>0) {
	    				if( rtName.charAt(rtName.length()-1)==' ' ) {
	    					continue;
	    				}
    				}
    			}
    			rtName = rtName + Convert(epName);

    		}


    	
//    	System.out.println(rtName);
    	return rtName;
		
	}
	public String Convert(char b){
		
//		if(b == ' ') {
//			return " ";
//		}
		
        String firstL = null;
        
        int first = (b - 44032 ) / ( 21 * 28 );

         switch(first){

             case 0:

             	firstL="ㄱ";

                 break;

             case 1:

                 firstL="ㄲ";

                 break;

             case 2:

                 firstL="ㄴ";

                 break;

             case 3:

             	firstL="ㄷ";

                 break;

             case 4:

                 firstL="ㄸ";

                 break;

             case 5:

                 firstL="ㄹ";

                 break;

             case 6:

                 firstL="ㅁ";

                 break;

             case 7:

             	firstL="ㅂ";

             	break;

             case 8:

                 firstL="ㅃ";

                 break;

             case 9:

             	firstL="ㅅ";

                 break;

             case 10:

                 firstL="ㅆ";

                 break;

             case 11:

                 firstL="ㅇ";

                 break;

             case 12:

             	firstL="ㅈ";

                 break;

             case 13:

                 firstL="ㅉ";

                 break;

             case 14:

                 firstL="ㅊ";

                 break;

             case 15:

                 firstL="ㅋ";

                 break;

             case 16:

                 firstL="ㅌ";

                 break;

             case 17:

                 firstL="ㅍ";

                 break;

             case 18:

                 firstL="ㅎ";

                 break;

             default: firstL=String.valueOf(b);
//             default: firstL=" ";

         }     

     

      return firstL;

	}


}
