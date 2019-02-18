package uk.co.firefly.library.rest.version0_1.handler;

import org.springframework.http.HttpStatus;

public class ApiErrorResponse {
	private String message;
	private HttpStatus status;
	private Long timestamp;

	public ApiErrorResponse() {
	}

	public ApiErrorResponse(String message, HttpStatus status, Long timestamp) {
		this.message = message;
		this.status = status;
		this.timestamp = timestamp;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

}
