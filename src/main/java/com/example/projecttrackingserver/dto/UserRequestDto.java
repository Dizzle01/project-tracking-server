package com.example.projecttrackingserver.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Class representing a user-related request.
 */
public record UserRequestDto (
		@NotBlank(message = "username should not be blank")
		String username
) {
}
