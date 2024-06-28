package com.example.projecttrackingserver.auth;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Class to handle unauthorized access attempts.
 */
@Component
public class UnauthorizedHandler implements AuthenticationEntryPoint {
	
    /**
     * {@inheritDoc}
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
    	response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
        System.out.println(authException.getMessage());
    }
}
