package com.example.projecttrackingserver.exceptions;

/**
 * Exception thrown when an entity does not exist in the database
 */
public class EntityNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1739713694798028657L;

    /**
     * Constructs an EntityNotFoundException with a formatted error message including possible alternatives.
     *
     * @param property     the property or field name causing the error
     * @param value        the value that was searched for but not found
     * @param possibilities suggestions or alternatives for the value
     */
	public EntityNotFoundException(String property, String value, String posibilities) {
    	super(String.format("%s property with value %s not found, try -> %s", property, value, posibilities));
    }
    
    /**
     * Constructs an EntityNotFoundException with a formatted error message for string values.
     *
     * @param property the property or field name causing the error
     * @param value    the string value that was searched for but not found
     */
    public EntityNotFoundException(String property, String value) {
    	super(String.format("%s with value %s not found", property, value));
    }
    
    /**
     * Constructs an EntityNotFoundException with a formatted error message for long values.
     *
     * @param property the property or field name causing the error
     * @param value    the long value that was searched for but not found
     */
    public EntityNotFoundException(String property, long value) {
    	super(String.format("%s with value %d not found", property, value));
    }
}
