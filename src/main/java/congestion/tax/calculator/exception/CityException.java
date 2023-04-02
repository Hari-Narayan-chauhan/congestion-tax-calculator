package congestion.tax.calculator.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class CityException extends Exception {

	private String message;
	private HttpStatus httpStatus;

	public CityException(String message) {
        this.message = message;
    }

	public CityException(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }

}
