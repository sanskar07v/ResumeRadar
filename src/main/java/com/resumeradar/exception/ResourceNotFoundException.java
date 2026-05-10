package com.resumeradar.exception;

public class ResourceNotFoundException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = -6444856449804033642L;

	public ResourceNotFoundException(String message) {
        super(message);
    }
}
