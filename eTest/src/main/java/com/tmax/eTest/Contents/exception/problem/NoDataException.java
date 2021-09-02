package com.tmax.eTest.Contents.exception.problem;

public class NoDataException extends Exception{
	public NoDataException(){
		super();
	}
	public NoDataException(long id){
		super("No data for ID = " + Long.toString(id));
	}
	public NoDataException(String input) {
		super("No data for the condition : " + input);
	}
	
}
