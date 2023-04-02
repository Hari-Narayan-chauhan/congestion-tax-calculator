package congestion.tax.calculator.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class VehicleException extends Exception {

	private String message;
	private HttpStatus httpStatus;

	public VehicleException(String message) {
        this.message = message;
    }

	public VehicleException(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }

}
