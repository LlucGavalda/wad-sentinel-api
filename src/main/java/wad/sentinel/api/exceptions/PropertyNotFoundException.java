package wad.sentinel.api.exceptions;

import wad.sentinel.api.error.ErrorConstants;

public class PropertyNotFoundException extends AbstractException {

    private static final long serialVersionUID = 5963475322179468883L;

    /**
     * Parameter cannot be empty Exception
     * 
     * @param code
     */
    public PropertyNotFoundException(String property) {
        super(ErrorConstants.ERROR_PROPERTY_NOT_FOUND,
                new String[] { property },
                String.format("Property has not been found: %s", property));

    }

    /**
     * Parameter corresponding to an entity field cannot be empty Exception
     * 
     * @param entity
     * @param code
     */
    public PropertyNotFoundException(String entity, String property) {
        super(ErrorConstants.ERROR_PROPERTY_NOT_FOUND,
                new String[] { entity + "." + property },
                String.format("Property has not been found: %s.%s", entity, property));

    }

}