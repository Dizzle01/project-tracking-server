package com.example.projecttrackingserver.dto;

import java.time.LocalDate;

import com.example.projecttrackingserver.enums.TicketPriority;
import com.example.projecttrackingserver.enums.TicketStatus;
import com.example.projecttrackingserver.enums.TicketType;

/**
 * Class representing a ticket-related response.
 */
public record TicketResponseDto(
		long id,
		String name,
		String description,
		LocalDate createdAt,
		LocalDate updatedAt,
		long projectId,
		long creatorId,
		TicketType ticketType,
		TicketPriority ticketPriority,
		TicketStatus ticketStatus
){	
}
