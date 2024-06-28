package com.example.projecttrackingserver.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.projecttrackingserver.entities.TicketStatusEntity;
import com.example.projecttrackingserver.enums.TicketStatus;

/**
 * Repository interface for performing CRUD operations on TicketStatusEntity.
 */
@Repository
public interface TicketStatusRepository extends CrudRepository<TicketStatusEntity, Long> {

    /**
     * Retrieves an optional TicketStatusEntity by its ticketStatus.
     *
     * @param ticketStatus the TicketStatus enum value to search for
     * @return an Optional containing the TicketStatusEntity if found, otherwise empty
     */
	Optional<TicketStatusEntity> findByTicketStatus(TicketStatus ticketStatus);
}
