package com.example.projecttrackingserver.auth;

import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

import com.example.projecttrackingserver.entities.UserEntity;
import com.example.projecttrackingserver.services.UserServiceImpl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

/**
 * Class responsible for extracting and validating API key authentication from HTTP requests.
 */
@Component
@RequiredArgsConstructor
public class ApiKeyAuthExtractor {

	private final UserServiceImpl userService;

    /**
     * Extracts authentication information from the provided request.
     *
     * @param request the request containing the API key in the header
     * @return an Optional containing Authentication if the API key is valid, otherwise empty
     */
    public Optional<Authentication> extract(HttpServletRequest request) {
    	String providedKey = request.getHeader("ApiKey");
    	Optional<UserEntity> userOptional = userService.getEntityByAPIKey(providedKey);
        if (providedKey == null || userOptional.isEmpty()) {
        	return Optional.empty();
        }

        return Optional.of(new ApiKeyAuth(userOptional.get(), AuthorityUtils.NO_AUTHORITIES));
    }
}