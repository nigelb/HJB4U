package hjb4u.exceptions;

/**
 * Date: 29/09/2010
 * Time: 4:55:41 PM
 *
 * @Author Nigel Bajema
 */
public class NamespaceException extends RuntimeException{
	public NamespaceException() {
	}

	public NamespaceException(Throwable cause) {
		super(cause);
	}

	public NamespaceException(String message) {
		super(message);
	}

	public NamespaceException(String message, Throwable cause) {
		super(message, cause);
	}
}
