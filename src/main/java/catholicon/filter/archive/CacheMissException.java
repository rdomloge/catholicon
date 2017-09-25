package catholicon.filter.archive;

@SuppressWarnings("serial")
public class CacheMissException extends RuntimeException {

	public CacheMissException(String message, Throwable cause) {
		super(message, cause);
	}

	public CacheMissException(String message) {
		super(message);
	}

	public CacheMissException(Throwable cause) {
		super(cause);
	}

}
