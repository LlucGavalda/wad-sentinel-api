package wad.sentinel.api.exceptions;

import wad.sentinel.api.error.ErrorConstants;

public class BadLogicalOperatorsException extends AbstractException {

    private static final long serialVersionUID = -1412628129292175349L;

    /**
     * File not found Exception
     * 
     * @param message
     */
    public BadLogicalOperatorsException() {
        super(ErrorConstants.ERROR_BAD_LOGICAL_OPERATORS, String.format("Bad query AND/OR operators"));

    }
}
