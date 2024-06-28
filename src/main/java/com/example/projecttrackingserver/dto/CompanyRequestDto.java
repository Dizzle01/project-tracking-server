package com.example.projecttrackingserver.dto;

/**
 * Class representing a company-related request.
 */
public record CompanyRequestDto(
		String name,
		String description
) {
}
