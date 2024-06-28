package com.example.projecttrackingserver.services;

import java.util.Optional;

import com.example.projecttrackingserver.entities.TicketStatusEntity;
import com.example.projecttrackingserver.enums.TicketStatus;

/**
 * Service interface related to ticket statuses.
 */
public interface TicketStatusService {

    /**
     * Retrieves an Optional of TicketStatusEntity by ticket status.
     *
     * @param ticketStatus the TicketStatus enum value
     * @return an Optional containing the TicketStatusEntity if found, otherwise empty
     */
	Optional<TicketStatusEntity> getEntityByTicketStatus(TicketStatus ticketStatus);
	
    /**
     * Retrieves a string representation of all ticket statuses.
     *
     * @return a string representation of all ticket statuses
     */
	String getAllTicketStatusesString();
}
