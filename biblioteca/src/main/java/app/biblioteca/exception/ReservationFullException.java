package app.biblioteca.exception;

public class ReservationFullException extends RuntimeException {
	public ReservationFullException(String message) {
		super(message);
	}
}