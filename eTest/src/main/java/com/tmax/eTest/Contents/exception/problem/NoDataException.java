package com.tmax.eTest.Contents.exception.problem;

public class NoDataException extends Exception{
	public NoDataException(){
		super();
	}
	public NoDataException(long id){
		super(Long.toString(id)+"there is no data");
	}
	public NoDataException(String input) {
		super(input+" = there is no data");
	}
	
}
