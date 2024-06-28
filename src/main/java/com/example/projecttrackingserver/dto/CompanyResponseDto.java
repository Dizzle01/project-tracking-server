package com.example.projecttrackingserver.dto;

/**
 * Class representing a company-related response.
 */
public record CompanyResponseDto(
		long id,
		String name,
		String description
) {
}