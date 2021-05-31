package com.tmax.eTest.Contents.exception.problem;

public class NoDataException extends Exception{
	public NoDataException(){
		super();
	}
	public NoDataException(long id){
		super(Long.toString(id)+"에 해당하는 데이터가 없습니다.");
	}
}
