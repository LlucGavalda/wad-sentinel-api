package wad.sentinel.api.exceptions;

import wad.sentinel.api.error.ErrorConstants;

public class ParameterInvalidException extends AbstractException {

    private static final long serialVersionUID = -4525556536504615561L;

    /**
     * Invalid value for a parameter exception
     * 
     * @param param
     * @param value
     */
    public ParameterInvalidException(String param, String value) {
        super(ErrorConstants.ERROR_PARAMETER_INVALID,
                new String[] { param, value },
                String.format("Invalid value for %s: %s", param, value));

    }

}
