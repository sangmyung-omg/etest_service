package com.tmax.eTest.Contents.exception.problem;

import java.util.Arrays;

public class UnavailableTypeException extends Exception {
	
		public UnavailableTypeException(){
			super();
		}
		public UnavailableTypeException(String[] availableType, String type){
			super(type+" is an unavailable input. Available types are: "+Arrays.toString(availableType));
		}
		public UnavailableTypeException(String type){
			super(type+" is an unavailable input.");
		}
	
}
