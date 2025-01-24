package com.skymicrosystems.controleestoque.exceptions;

public class CodigoDuplicadoException extends Exception {

	private static final long serialVersionUID = 5326395146917024708L;
	
	public CodigoDuplicadoException() {
		super();
	}
	
	public CodigoDuplicadoException(String message) {
		super(message);
	}
	
	public CodigoDuplicadoException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
