package com.example.projecttrackingserver.services;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Service;

import com.example.projecttrackingserver.entities.TicketPriorityEntity;
import com.example.projecttrackingserver.enums.TicketPriority;
import com.example.projecttrackingserver.repositories.TicketPriorityRepository;

import lombok.RequiredArgsConstructor;

/**
 * Implementation of {@link TicketPriorityService} interface 
 */
@Service
@RequiredArgsConstructor
public class TicketPriorityServiceImpl implements TicketPriorityService {

	private final TicketPriorityRepository ticketPriorityRepository;
	
    /**
     * {@inheritDoc}
     */
	public Optional<TicketPriorityEntity> getEntityByTicketPriority(TicketPriority ticketPriority) {
		return ticketPriorityRepository.findByTicketPriority(ticketPriority);
	}
	
    /**
     * {@inheritDoc}
     */
	public String getAllTicketPrioritiesString() {
		return StreamSupport.stream(ticketPriorityRepository.findAll().spliterator(), false)
					 		.map(ticketPriorityEntity -> ticketPriorityEntity.getTicketPriority().toString())
					 		.collect(Collectors.joining(", "));
	}
}
