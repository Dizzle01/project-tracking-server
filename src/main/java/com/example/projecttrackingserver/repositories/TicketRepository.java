package com.example.projecttrackingserver.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.projecttrackingserver.entities.TicketEntity;

/**
 * Repository interface for performing CRUD operations on TicketEntity.
 */
@Repository
public interface TicketRepository extends CrudRepository<TicketEntity, Long> {

    /**
     * Retrieves an optional TicketEntity by its name and projectId.
     *
     * @param name the name of the ticket to search for
     * @param projectId the ID of the project associated with the ticket
     * @return an Optional containing the TicketEntity if found, otherwise empty
     */
	Optional<TicketEntity> findByNameAndProjectId(String name, long projectId);
	
    /**
     * Retrieves an optional TicketEntity by its ticketId and projectId.
     *
     * @param ticketId the ID of the ticket to search for
     * @param projectId the ID of the project associated with the ticket
     * @return an Optional containing the TicketEntity if found, otherwise empty
     */
	Optional<TicketEntity> findByIdAndProjectId(long ticketId, long projectId);
}
