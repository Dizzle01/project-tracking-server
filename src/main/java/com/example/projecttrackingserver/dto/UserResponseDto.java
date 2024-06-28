package com.example.projecttrackingserver.dto;

import com.example.projecttrackingserver.enums.Role;

/**
 * Class representing a user-related response.
 */
public record UserResponseDto (
		long id,
		String username,
		String company,
		Role role,
		String apiKey
) {
}