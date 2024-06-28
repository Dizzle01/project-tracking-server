package com.example.projecttrackingserver.services;

import java.util.List;
import java.util.Optional;

import com.example.projecttrackingserver.dto.TicketRequestDto;
import com.example.projecttrackingserver.dto.TicketResponseDto;
import com.example.projecttrackingserver.entities.TicketEntity;

/**
 * Service interface related to tickets.
 */
public interface TicketService {

    /**
     * Retrieves a list of TicketResponseDto for all tickets in a project.
     *
     * @param companyId the ID of the company
     * @param projectId the ID of the project
     * @return a list of TicketResponseDto
     */
	List<TicketResponseDto> getAllTicketDtosInProject(long companyId, long projectId);
	
    /**
     * Retrieves a TicketResponseDto for a specific ticket in a project.
     *
     * @param companyId the ID of the company
     * @param projectId the ID of the project
     * @param ticketId the ID of the ticket
     * @return a TicketResponseDto
     */
	TicketResponseDto getTicketDtoById(long companyId, long projectId, long ticketId);
	
    /**
     * Creates a new ticket in a project.
     *
     * @param ticketRequestDto the TicketRequestDto containing ticket details
     * @param companyId the ID of the company
     * @param projectId the ID of the project
     * @return the created TicketResponseDto
     */
	TicketResponseDto createTicket(TicketRequestDto ticketRequestDto, long companyId, long projectId);
	
    /**
     * Updates an existing ticket in a project.
     *
     * @param ticketRequestDto the TicketRequestDto containing updated ticket details
     * @param companyId the ID of the company
     * @param projectId the ID of the project
     * @param ticketId the ID of the ticket
     * @return the updated TicketResponseDto
     */
	TicketResponseDto updateTicket(TicketRequestDto ticketRequestDto, long companyId, long projectId, long ticketId);
	
    /**
     * Deletes a ticket from a project.
     *
     * @param companyId the ID of the company
     * @param projectId the ID of the project
     * @param ticketId the ID of the ticket to delete
     */
	void deleteTicket(long companyId, long projectId, long ticketId);
	
    /**
     * Checks if a ticket with the given name exists in a project.
     *
     * @param name the name of the ticket
     * @param projectId the ID of the project
     * @return true if the ticket exists, false otherwise
     */
	boolean ticketExists(String name, long projectId);
	
    /**
     * Retrieves an Optional of TicketEntity by ticket ID and project ID.
     *
     * @param ticketId the ID of the ticket
     * @param projectId the ID of the project
     * @return an Optional containing the TicketEntity if found, otherwise empty
     */
	Optional<TicketEntity> getEntityByIdAndProjectId(long ticketId, long projectId);
}
