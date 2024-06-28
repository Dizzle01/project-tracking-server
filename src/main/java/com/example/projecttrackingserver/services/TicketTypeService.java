package com.example.projecttrackingserver.services;

import java.util.Optional;

import com.example.projecttrackingserver.entities.TicketTypeEntity;
import com.example.projecttrackingserver.enums.TicketType;

/**
 * Service interface related to ticket types.
 */
public interface TicketTypeService {

    /**
     * Retrieves an Optional of TicketTypeEntity by ticket type.
     *
     * @param ticketType the TicketType enum value
     * @return an Optional containing the TicketTypeEntity if found, otherwise empty
     */
	Optional<TicketTypeEntity> getEntityByTicketType(TicketType ticketType);
	
    /**
     * Retrieves a string representation of all ticket types.
     *
     * @return a string representation of all ticket types
     */
	String getAllTicketTypesString();
}
