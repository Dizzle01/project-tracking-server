package com.example.projecttrackingserver.auth;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import com.example.projecttrackingserver.entities.UserEntity;

/**
 * Custom authentication token for API key authentication.
 * This token is used to authenticate a user based on their API key.
 */
public class ApiKeyAuth extends AbstractAuthenticationToken {

	private static final long serialVersionUID = -3883285009363141945L;
	
	private final UserEntity authenticatedUser;

    /**
     * Constructs a new ApiKeyAuth token with the given authenticated user and authorities.
     *
     * @param authenticatedUser the authenticated user entity
     * @param authorities the collection of granted authorities
     */
    public ApiKeyAuth(UserEntity authenticatedUser, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.authenticatedUser = authenticatedUser;
        setAuthenticated(true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getCredentials() {
    	return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getPrincipal() {
    	return authenticatedUser;
    }
}