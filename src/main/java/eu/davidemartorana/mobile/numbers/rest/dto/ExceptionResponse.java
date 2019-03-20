package eu.davidemartorana.mobile.numbers.rest.dto;

import java.util.Date;

/**
 * 
 * @author davidemartorana
 *
 */
public class ExceptionResponse {

	private String message;
	
	private int status;
	
	private String error;
	
	private String path;
	
	private Date timestamp;

	public String getMessage() {
		return message;
	}

	public ExceptionResponse setMessage(String message) {
		this.message = message;
		return this;
	}

	public int getStatus() {
		return status;
	}

	public ExceptionResponse setStatus(int status) {
		this.status = status;
		return this;
	}

	public String getError() {
		return error;
	}

	public ExceptionResponse setError(String error) {
		this.error = error;
		return this;
	}

	public String getPath() {
		return path;
	}

	public ExceptionResponse setPath(String path) {
		this.path = path;
		return this;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public ExceptionResponse setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
		return this;
	}
	
	
	
}
