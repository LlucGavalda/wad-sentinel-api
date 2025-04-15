package wad.sentinel.api.exceptions;

import wad.sentinel.api.error.ErrorConstants;

public class BadQueryParenthisationException extends AbstractException {

    private static final long serialVersionUID = -1412628129292175349L;

    /**
     * File not found Exception
     * 
     * @param message
     */
    public BadQueryParenthisationException() {
        super(ErrorConstants.ERROR_BAD_QUERY_PARENTHISATION, String.format("Bad query parenthisation"));

    }
}
