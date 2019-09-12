package aide.database.exceptions;

public class FinderException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public FinderException() {
		super();
	}
	
	public FinderException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public FinderException(String message) {
		super(message);
	}
	
	public FinderException(Throwable cause) {
		super(cause);
	}
}
