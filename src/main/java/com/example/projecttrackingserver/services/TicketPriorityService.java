package com.example.projecttrackingserver.services;

import java.util.Optional;

import com.example.projecttrackingserver.entities.TicketPriorityEntity;
import com.example.projecttrackingserver.enums.TicketPriority;

/**
 * Service interface related to ticket priorities.
 */
public interface TicketPriorityService {

    /**
     * Retrieves a TicketPriorityEntity by its TicketPriority enum value.
     *
     * @param ticketPriority the TicketPriority enum value
     * @return an Optional containing the TicketPriorityEntity if found, otherwise empty
     */
	Optional<TicketPriorityEntity> getEntityByTicketPriority(TicketPriority ticketPriority);
	
    /**
     * Retrieves a string representation of all available ticket priorities.
     *
     * @return a string representing all available ticket priorities
     */
	String getAllTicketPrioritiesString();
}
