package com.example.projecttrackingserver.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Class representing a project-related request.
 */
public record ProjectRequestDto(
		@NotBlank(message = "projectname should not be blank")
		String projectName,
		String description,
		String startDate,
		String endDate,
		Long projectManagerId
) {
}
