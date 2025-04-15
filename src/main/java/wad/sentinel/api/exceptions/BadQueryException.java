package wad.sentinel.api.exceptions;

import wad.sentinel.api.error.ErrorConstants;

public class BadQueryException extends AbstractException {

    private static final long serialVersionUID = -1412628129292175349L;

    /**
     * File not found Exception
     * 
     * @param message
     */
    public BadQueryException() {
        super(ErrorConstants.ERROR_BAD_QUERY, String.format("Bad native query, error running it"));

    }
}
