package eu.davidemartorana.mobile.numbers.rest;

import java.util.Date;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import eu.davidemartorana.mobile.numbers.rest.dto.ExceptionResponse;

/**
 * 
 * @author davidemartorana
 *
 */
@RestControllerAdvice
public class GlobalErrorControllerAdvice extends ResponseEntityExceptionHandler {

	@ExceptionHandler(EmptyResultDataAccessException.class)
	@ResponseBody
	@ResponseStatus(code=HttpStatus.NOT_FOUND)
	public final ResponseEntity<ExceptionResponse> handleDataNotFound(final EmptyResultDataAccessException ex, final ServletWebRequest request) {
	    final ExceptionResponse exceptionResponse = new ExceptionResponse()
	    		.setTimestamp(new Date())
	    		.setMessage(ex.getMessage())
	    		.setStatus(HttpStatus.NOT_FOUND.value())
	    		.setError("Not Found.")
	    		.setPath(request.getRequest().getRequestURI());
	    
	    return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
	}
	
	
	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public final ResponseEntity<ExceptionResponse> handlerForIllegalArgument(final IllegalArgumentException ex, final ServletWebRequest request) {
	    final ExceptionResponse exceptionResponse = new ExceptionResponse()
	    		.setTimestamp(new Date())
	    		.setMessage(ex.getMessage())
	    		.setStatus(HttpStatus.BAD_REQUEST.value())
	    		.setError("Bad Request.")
	    		.setPath(request.getRequest().getRequestURI());
	    
	    return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(Exception.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public final ResponseEntity<ExceptionResponse> handleAllExceptions(final Exception ex, final ServletWebRequest request) {
	    final ExceptionResponse exceptionResponse = new ExceptionResponse()
	    		.setTimestamp(new Date())
	    		.setMessage(ex.getMessage())
	    		.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
	    		.setError("Internal Server Error.")
	    		.setPath(request.getRequest().getRequestURI());
	    
	    return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
}
