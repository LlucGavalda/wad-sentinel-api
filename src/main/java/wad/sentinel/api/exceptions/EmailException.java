package wad.sentinel.api.exceptions;

import wad.sentinel.api.error.ErrorConstants;

public class EmailException extends AbstractException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7932109661139709778L;

	/**
	 * EMail generic exception with a message.
	 * 
	 * @param msg Exception message
	 */
	public EmailException(String msg) {
		super(ErrorConstants.ERROR_EMAIL, msg);
	}
}