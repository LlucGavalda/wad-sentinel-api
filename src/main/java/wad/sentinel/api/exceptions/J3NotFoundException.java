package wad.sentinel.api.exceptions;

import wad.sentinel.api.error.ErrorConstants;

public class J3NotFoundException extends AbstractJ3Exception{
	
	private static final long serialVersionUID = 7948831832861112481L;
		
	/**
	 * Entity not found with <field> = <value> Exception
	 * 
	 * @param entity
	 * @param field
	 * @param value
	 */
	public J3NotFoundException(String entity, String field, String value) {
		super(ErrorConstants.ERROR_NOT_FOUND,
				new String[] {entity+"."+field, value},
				String.format("%s not found with %s: %s", entity, field, value));
	}
	
	/**
	 * Field not found Exception
	 * 
	 * @param entity
	 * @param field
	 */
	public J3NotFoundException(String entity, String field) {
		super(ErrorConstants.ERROR_NOT_FOUND,
				new String[] {entity, field},
				String.format("Field %s not found in entity %s", field, entity));
	}
	
	
}
