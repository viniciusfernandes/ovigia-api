package br.com.ovigia.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class GlobalException extends ResponseStatusException {

     

	/**
	 * 
	 */
	private static final long serialVersionUID = -4137678568737005917L;

	public GlobalException(HttpStatus status, String message) {
        super(status, message);
    }

    public GlobalException(HttpStatus status, String message, Throwable e) {
        super(status, message, e);
    }
}