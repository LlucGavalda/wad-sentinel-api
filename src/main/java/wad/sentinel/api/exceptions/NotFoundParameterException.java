package wad.sentinel.api.exceptions;

import wad.sentinel.api.error.ErrorConstants;

public class NotFoundParameterException extends AbstractException {

	private static final long serialVersionUID = -2571342308286335569L;

	/**
	 * Parameter not found Exception
	 * 
	 * @param code
	 */
	public NotFoundParameterException(String code) {
		super(ErrorConstants.ERROR_PARAMETER_NOT_FOUND,
				new String[] { code },
				String.format("Parameter %s not defined", code));

	}

}
