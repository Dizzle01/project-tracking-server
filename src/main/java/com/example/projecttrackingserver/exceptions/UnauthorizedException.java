package com.example.projecttrackingserver.exceptions;

/**
 * Exception thrown when user is not authorized.
 */
public class UnauthorizedException extends RuntimeException {

	private static final long serialVersionUID = -521888211079627817L;

    /**
     * Constructs an UnauthorizedException with a default error message.
     */
	public UnauthorizedException() {
    	super("you are not authorized for this action");
    }
    
    /**
     * Constructs an UnauthorizedException with a formatted error message indicating which user is not authorized.
     *
     * @param userId the ID of the user who is not authorized
     */
    public UnauthorizedException(long userId) {
    	super(String.format("user with id %d is not authorized for this", userId));
    }
}
