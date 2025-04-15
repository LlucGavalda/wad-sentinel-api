package wad.sentinel.api.exceptions;

/**
 * Abstract class for all Exceptions common elements
 */
public abstract class AbstractException extends RuntimeException {

	private static final long serialVersionUID = -7479889156146341850L;

	private int errorCode;
	private String[] parameters;

	/**
	 * Constructor with code and message
	 * 
	 * @param errorCode
	 * @param message
	 */
	protected AbstractException(int errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}

	/**
	 * Constructor with code, message and paramters
	 * 
	 * @param errorCode
	 * @param params
	 * @param message
	 */
	protected AbstractException(int errorCode, String[] params, String message) {
		super(message);
		this.errorCode = errorCode;
		this.parameters = params;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String[] getParameters() {
		return parameters;
	}

	public void setParameters(String[] parameters) {
		this.parameters = parameters;
	}

}
