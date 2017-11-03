package catholicon.ex;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class UnsupportedParametersException extends RuntimeException {

	public UnsupportedParametersException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnsupportedParametersException(String message) {
		super(message);
	}

}
