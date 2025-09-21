package app.biblioteca.exception;

import java.time.LocalDateTime;
import java.util.Map;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ErrorResponse {
	private int status;
	private String message;
	private LocalDateTime timestamp;
	private Map<String, String> errors;
	
	public ErrorResponse(int status, String message, Map<String, String> errors) {
		this.status = status;
		this.message = message;
		this.timestamp = LocalDateTime.now();
		this.errors = errors;
	}
}
