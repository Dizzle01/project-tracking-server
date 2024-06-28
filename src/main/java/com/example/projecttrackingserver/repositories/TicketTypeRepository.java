package com.example.projecttrackingserver.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.projecttrackingserver.entities.TicketTypeEntity;
import com.example.projecttrackingserver.enums.TicketType;

/**
 * Repository interface for performing CRUD operations on TicketTypeEntity.
 */
@Repository
public interface TicketTypeRepository extends CrudRepository<TicketTypeEntity, Long> {

    /**
     * Retrieves an optional TicketTypeEntity by its ticketType.
     *
     * @param ticketType the TicketType enum value to search for
     * @return an Optional containing the TicketTypeEntity if found, otherwise empty
     */
	Optional<TicketTypeEntity> findByTicketType(TicketType ticketType);
}
