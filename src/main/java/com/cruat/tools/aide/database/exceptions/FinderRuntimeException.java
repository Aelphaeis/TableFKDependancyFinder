package com.cruat.tools.aide.database.exceptions;

public class FinderRuntimeException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	public FinderRuntimeException() {
		super();
	}
	
	public FinderRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public FinderRuntimeException(String message) {
		super(message);
	}
	
	public FinderRuntimeException(Throwable cause) {
		super(cause);
	}
}
