package com.tmax.eTest.TestStudio.controller.component.exception;

import lombok.Getter;

@Getter
public class NoDataExceptionTs extends Exception{
	
	String securedMessage;
	
	public NoDataExceptionTs(){
		super();
	}
	public NoDataExceptionTs(long id){
//		super("there is no data"+" ID: "+Long.toString(id));
		this.securedMessage = "there is no data"+" ID: "+Long.toString(id);
	}
	public NoDataExceptionTs(String input) {
//		super("there is no data"+input);
		this.securedMessage = "there is no data"+input;
	}
	public NoDataExceptionTs(String input, String input2) {
//		super("there is no data"+" in "+input+", ID: "+input2);
		this.securedMessage = "there is no data"+" in "+input+", ID: "+input2;
	}
}
