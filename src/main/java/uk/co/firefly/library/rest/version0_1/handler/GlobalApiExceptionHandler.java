package uk.co.firefly.library.rest.version0_1.handler;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import uk.co.firefly.library.rest.version0_1.exception.BadRequestException;
import uk.co.firefly.library.rest.version0_1.exception.PayloadException;
import uk.co.firefly.library.rest.version0_1.exception.ResourceNotFoundException;

@RestControllerAdvice
public class GlobalApiExceptionHandler {
	
	@ExceptionHandler(ResourceNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ApiErrorResponse runResourceNotFound(ResourceNotFoundException exception) {
		ApiErrorResponse response = new ApiErrorResponse(
				exception.getLocalizedMessage(), HttpStatus.NOT_FOUND, (new Date()).getTime());
		return response;
	}

	@ExceptionHandler(BadRequestException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ApiErrorResponse runBadRequest(BadRequestException exception) {
		ApiErrorResponse response = new ApiErrorResponse(
				exception.getLocalizedMessage(), HttpStatus.BAD_REQUEST, (new Date()).getTime());
		return response;
	}

	@ExceptionHandler(PayloadException.class)
	@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
	public ApiErrorResponse runPayload(PayloadException exception) {
		ApiErrorResponse response = new ApiErrorResponse(
				exception.getLocalizedMessage(), HttpStatus.NOT_ACCEPTABLE, (new Date()).getTime());
		return response;
	}
}
