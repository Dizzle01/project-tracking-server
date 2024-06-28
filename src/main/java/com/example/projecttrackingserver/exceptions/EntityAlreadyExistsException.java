package com.example.projecttrackingserver.exceptions;

/**
 * Exception thrown when an entity already exists in the database
 */
public class EntityAlreadyExistsException extends RuntimeException {

	private static final long serialVersionUID = 2384994739650464262L;

    /**
     * Constructs an EntityAlreadyExistsException with a formatted error message.
     *
     * @param jsonProperty the JSON property or field name causing the conflict
     * @param entityName   the name or identifier of the entity that already exists
     */
	public EntityAlreadyExistsException(String jsonProperty, String entityName) {
    	super(String.format("%s with value %s already exists", jsonProperty, entityName));
    }
}
