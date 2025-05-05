package wad.sentinel.api.exceptions;

import wad.sentinel.api.error.ErrorConstants;

public class ParameterEmptyException extends AbstractException {

	private static final long serialVersionUID = -2571342308286335569L;

	/**
	 * Parameter cannot be empty Exception
	 * 
	 * @param code
	 */
	public ParameterEmptyException(String code) {
		super(ErrorConstants.ERROR_PARAMETER_EMPTY,
				new String[] { code },
				String.format("Parameter %s cannot be empty", code));
	}

	/**
	 * Parameter cannot be empty Exception
	 * 
	 * @param entity
	 * @param code
	 */
	public ParameterEmptyException(String entity, String code) {
		super(ErrorConstants.ERROR_PARAMETER_EMPTY,
				new String[] { entity + "." + code },
				String.format("Parameter %s.%s cannot be empty", entity, code));
	}

}
