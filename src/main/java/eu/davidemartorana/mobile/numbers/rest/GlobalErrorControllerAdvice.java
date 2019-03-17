package eu.davidemartorana.mobile.numbers.rest;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 
 * @author davidemartorana
 *
 */
@RestControllerAdvice
public class GlobalErrorControllerAdvice {

	@ExceptionHandler(EmptyResultDataAccessException.class)
	@ResponseBody
	@ResponseStatus(code=HttpStatus.NOT_FOUND)
	public String dataNotFound(final EmptyResultDataAccessException e) {
		return "Data Not Found";
	}
	
	
	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public String handlerForIllegalArgument(final IllegalArgumentException ex) {
		return ex.getMessage();
	}
	
}
