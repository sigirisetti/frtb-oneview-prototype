package com.quark.core.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * Exception message should be derived from code.
 * 
 * When no mapping for code is found then supplied message is used
 * 
 * @author Surya
 */
@Getter
@Setter
public class ApplicationException extends Exception {

	private static final long serialVersionUID = 1L;

	private int code;
	private String message;

	public ApplicationException(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public ApplicationException(int code, String message, Throwable t) {
		super(message, t);
		this.code = code;
	}
}
