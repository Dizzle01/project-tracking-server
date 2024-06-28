package com.example.projecttrackingserver.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Class representing a ticket-related request.
 */
public record TicketRequestDto(
		@NotBlank(message = "name should not be blank")
		String name,
		String description,
		@NotBlank(message = "ticketType should not be blank")
		String ticketType,
		@NotBlank(message = "ticketPriority should not be blank")
		String ticketPriority,
		@NotBlank(message = "ticketStatus should not be blank")
		String ticketStatus
) {
}
