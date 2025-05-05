package wad.sentinel.api.exceptions;

import wad.sentinel.api.error.ErrorConstants;

public class StorageException extends AbstractException {

	private static final long serialVersionUID = 4091891354173067973L;

	/**
	 * Generic storage exception.
	 * 
	 * @param message
	 */
	public StorageException(String message) {
		super(ErrorConstants.ERROR_STORAGE, String.format("Storage error: %s", message));
	}

	/**
	 * Generic storage exception.
	 * 
	 * @param message
	 */
	public StorageException(String message, Exception e) {
		super(ErrorConstants.ERROR_STORAGE,
				new String[] { message, e.getMessage() },
				String.format("Storage error: %s. Exception: %s", message, e.getMessage()));
	}

}
