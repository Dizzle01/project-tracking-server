package com.example.projecttrackingserver.services;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Service;

import com.example.projecttrackingserver.entities.TicketStatusEntity;
import com.example.projecttrackingserver.enums.TicketStatus;
import com.example.projecttrackingserver.repositories.TicketStatusRepository;

import lombok.RequiredArgsConstructor;

/**
 * Implementation of {@link TicketStatusService} interface 
 */
@Service
@RequiredArgsConstructor
public class TicketStatusServiceImpl implements TicketStatusService {

	private final TicketStatusRepository ticketStatusRepository;
	
    /**
     * {@inheritDoc}
     */
	public Optional<TicketStatusEntity> getEntityByTicketStatus(TicketStatus ticketStatus) {
		return ticketStatusRepository.findByTicketStatus(ticketStatus);
	}
	
    /**
     * {@inheritDoc}
     */
	public String getAllTicketStatusesString() {
		return StreamSupport.stream(ticketStatusRepository.findAll().spliterator(), false)
					 		.map(ticketStatusEntity -> ticketStatusEntity.getTicketStatus().toString())
					 		.collect(Collectors.joining(", "));
	}
}
