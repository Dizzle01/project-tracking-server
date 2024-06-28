package com.example.projecttrackingserver.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.projecttrackingserver.entities.TicketPriorityEntity;
import com.example.projecttrackingserver.enums.TicketPriority;

/**
 * Repository interface for performing CRUD operations on TicketPriorityEntity.
 */
@Repository
public interface TicketPriorityRepository extends CrudRepository<TicketPriorityEntity, Long> {

    /**
     * Retrieves an optional TicketPriorityEntity by its TicketPriority enumeration value.
     *
     * @param ticketPriorityName the TicketPriority enumeration value to search for
     * @return an Optional containing the TicketPriorityEntity if found, otherwise empty
     */
	Optional<TicketPriorityEntity> findByTicketPriority(TicketPriority ticketPriorityName);
}
