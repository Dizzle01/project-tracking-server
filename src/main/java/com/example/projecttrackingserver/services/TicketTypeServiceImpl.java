package com.example.projecttrackingserver.services;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Service;

import com.example.projecttrackingserver.entities.TicketTypeEntity;
import com.example.projecttrackingserver.enums.TicketType;
import com.example.projecttrackingserver.repositories.TicketTypeRepository;

import lombok.RequiredArgsConstructor;

/**
 * Implementation of {@link TicketTypeService} interface 
 */
@Service
@RequiredArgsConstructor
public class TicketTypeServiceImpl implements TicketTypeService {

	private final TicketTypeRepository ticketTypeRepository;
	
    /**
     * {@inheritDoc}
     */
	public Optional<TicketTypeEntity> getEntityByTicketType(TicketType ticketType) {
		return ticketTypeRepository.findByTicketType(ticketType);
	}
	
    /**
     * {@inheritDoc}
     */
	public String getAllTicketTypesString() {
		return StreamSupport.stream(ticketTypeRepository.findAll().spliterator(), false)
					 		.map(ticketTypeEntity -> ticketTypeEntity.getTicketType().toString())
					 		.collect(Collectors.joining(", "));
	}
}
