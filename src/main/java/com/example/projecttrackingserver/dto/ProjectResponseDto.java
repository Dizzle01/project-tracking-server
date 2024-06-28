package com.example.projecttrackingserver.dto;

import java.time.LocalDate;

/**
 * Class representing a project-related response.
 */
public record ProjectResponseDto(
		long id,
		String projectName,
		String description,
		LocalDate startDate,
		LocalDate endDate,
		long projectManagerId	
) {
}
