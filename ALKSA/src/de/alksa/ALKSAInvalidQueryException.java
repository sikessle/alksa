package de.alksa;

/**
 * May be thrown if a query is invalid or currently not supported by ALKSA or
 * the parser.
 */
public class ALKSAInvalidQueryException extends Exception {

	private static final long serialVersionUID = 1L;

	public ALKSAInvalidQueryException() {
		super();
	}

	public ALKSAInvalidQueryException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ALKSAInvalidQueryException(String message, Throwable cause) {
		super(message, cause);
	}

	public ALKSAInvalidQueryException(String message) {
		super(message);
	}

	public ALKSAInvalidQueryException(Throwable cause) {
		super(cause);
	}

}
