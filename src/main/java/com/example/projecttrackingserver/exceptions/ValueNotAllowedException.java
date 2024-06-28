package com.example.projecttrackingserver.exceptions;

/**
 * Exception thrown when a value is not allowed.
 */
public class ValueNotAllowedException extends RuntimeException {

	private static final long serialVersionUID = 1919199130329523259L;

    /**
     * Constructs a ValueNotAllowedException with a formatted error message indicating which property and value are not allowed.
     *
     * @param property the name of the property for which the value is not allowed
     * @param value the value that is not allowed for the property
     */
	public ValueNotAllowedException(String property, String value) {
    	super(String.format("property %s with value %s not allowed", property, value));
    }
}
