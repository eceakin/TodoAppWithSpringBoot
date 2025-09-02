package com.eceakin.todoAppSpring.shared.exceptions;


public class BusinessException extends RuntimeException{

	public BusinessException(String message) {
		super(message);
	}
}