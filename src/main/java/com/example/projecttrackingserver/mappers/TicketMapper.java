package com.example.projecttrackingserver.mappers;

import java.util.Optional;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.projecttrackingserver.dto.TicketRequestDto;
import com.example.projecttrackingserver.dto.TicketResponseDto;
import com.example.projecttrackingserver.entities.ProjectEntity;
import com.example.projecttrackingserver.entities.TicketEntity;
import com.example.projecttrackingserver.entities.TicketPriorityEntity;
import com.example.projecttrackingserver.entities.TicketStatusEntity;
import com.example.projecttrackingserver.entities.TicketTypeEntity;
import com.example.projecttrackingserver.entities.UserEntity;
import com.example.projecttrackingserver.enums.TicketPriority;
import com.example.projecttrackingserver.enums.TicketStatus;
import com.example.projecttrackingserver.enums.TicketType;
import com.example.projecttrackingserver.exceptions.EntityNotFoundException;
import com.example.projecttrackingserver.services.TicketPriorityServiceImpl;
import com.example.projecttrackingserver.services.TicketStatusServiceImpl;
import com.example.projecttrackingserver.services.TicketTypeServiceImpl;

/**
 * Mapper interface for mapping ticket-related classes.
 */
@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class TicketMapper {
	
	@Autowired
	protected  TicketStatusServiceImpl ticketStatusService;
	
	@Autowired
	protected  TicketPriorityServiceImpl ticketPriorityService;
	
	@Autowired
	protected  TicketTypeServiceImpl ticketTypeService;
	
	/**
	 * Maps TicketRequestDto along with associated ProjectEntity and UserEntity to TicketEntity.
	 * 
	 * @param dto the TicketRequestDto to map
	 * @param projectEntity the associated ProjectEntity
	 * @param creatorEntity the UserEntity representing the creator of the ticket
	 * @return the mapped TicketEntity
	 */
	@Mapping(target = "id", ignore = true)
	@Mapping(source = "dto.name", target = "name")
	@Mapping(source = "dto.description", target = "description")
	@Mapping(target = "createdAt", expression = "java(LocalDate.now())")
	@Mapping(target = "updatedAt", expression = "java(null)")
	@Mapping(source = "projectEntity", target = "project")
	@Mapping(source = "creatorEntity", target = "creator")
	public abstract TicketEntity toEntity(TicketRequestDto dto, ProjectEntity projectEntity, UserEntity creatorEntity);
	
	/**
	 * Maps TicketEntity to TicketResponseDto.
	 * 
	 * @param entity the TicketEntity to map
	 * @return the mapped TicketResponseDto
	 */
	@Mapping(source = "project.id", target = "projectId")
	@Mapping(source = "creator.id", target = "creatorId")
	@Mapping(source = "ticketStatus.ticketStatus", target = "ticketStatus")
	@Mapping(source = "ticketPriority.ticketPriority", target = "ticketPriority")
	@Mapping(source = "ticketType.ticketType", target = "ticketType")
	public abstract TicketResponseDto toDto(TicketEntity entity);
	
	/**
	 * Updates an existing TicketEntity with values from TicketRequestDto.
	 * 
	 * @param ticketEntity the existing TicketEntity to update
	 * @param dto the TicketRequestDto containing updated values
	 * @return the updated TicketEntity
	 */
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "project", ignore = true)
	@Mapping(target = "creator", ignore = true)
	@Mapping(source = "name", target = "name", defaultExpression = "java(ticketEntity.getName())")
	@Mapping(source = "description", target = "description", defaultExpression = "java(ticketEntity.getDescription())")
	@Mapping(target = "updatedAt", expression = "java(LocalDate.now())")
	@Mapping(source = "ticketStatus", target = "ticketStatus", defaultExpression = "java(ticketEntity.getTicketStatus())")
	@Mapping(source = "ticketPriority", target = "ticketPriority", defaultExpression = "java(ticketEntity.getTicketPriority())")
	@Mapping(source = "ticketType", target = "ticketType", defaultExpression = "java(ticketEntity.getTicketType())")
	public abstract TicketEntity updateEntity(@MappingTarget TicketEntity ticketEntity, TicketRequestDto dto);
	
	/**
	 * Maps a String representation of TicketType to TicketTypeEntity.
	 * 
	 * @param value the String value representing TicketType
	 * @return the mapped TicketTypeEntity
	 * @throws EntityNotFoundException if the provided TicketType value is not found
	 */
	protected TicketTypeEntity mapTicketType(String value) {
		TicketTypeEntity ticketTypeEntity;
		try {
			TicketType ticketType = TicketType.valueOf(value);
			Optional<TicketTypeEntity> ticketTypeOptional = ticketTypeService.getEntityByTicketType(ticketType);
			if(ticketTypeOptional.isEmpty()) {
				throw new EntityNotFoundException("ticketType", value, ticketTypeService.getAllTicketTypesString());
			}
			ticketTypeEntity = ticketTypeOptional.get();
		} catch(IllegalArgumentException ex) {
			throw new EntityNotFoundException("ticketType", value, ticketTypeService.getAllTicketTypesString());
		}
		
		return ticketTypeEntity;
	}
	
	/**
	 * Maps a String representation of TicketPriority to TicketPriorityEntity.
	 * 
	 * @param value the String value representing TicketPriority
	 * @return the mapped TicketPriorityEntity
	 * @throws EntityNotFoundException if the provided TicketPriority value is not found
	 */
	protected TicketPriorityEntity mapTicketPriority(String value) {
		TicketPriorityEntity ticketPriorityEntity;
		try {
			TicketPriority ticketPriority = TicketPriority.valueOf(value);
			Optional<TicketPriorityEntity> ticketPriorityOptional = ticketPriorityService.getEntityByTicketPriority(ticketPriority);
			if(ticketPriorityOptional.isEmpty()) {
				throw new EntityNotFoundException("ticketPriority", value, ticketPriorityService.getAllTicketPrioritiesString());
			}
			ticketPriorityEntity = ticketPriorityOptional.get();
		} catch(IllegalArgumentException ex) {
			throw new EntityNotFoundException("ticketPriority", value, ticketPriorityService.getAllTicketPrioritiesString());
		}
		
		return ticketPriorityEntity;
	}
	
	/**
	 * Maps a String representation of TicketStatus to TicketStatusEntity.
	 * 
	 * @param value the String value representing TicketStatus
	 * @return the mapped TicketStatusEntity
	 * @throws EntityNotFoundException if the provided TicketStatus value is not found
	 */
	protected TicketStatusEntity mapTicketStatus(String value) {
		TicketStatusEntity ticketStatusEntity;
		try {
			TicketStatus ticketStatus = TicketStatus.valueOf(value);
			Optional<TicketStatusEntity> ticketStatusOptional = ticketStatusService.getEntityByTicketStatus(ticketStatus);
			if(ticketStatusOptional.isEmpty()) {
				throw new EntityNotFoundException("ticketStatus", value, ticketStatusService.getAllTicketStatusesString());
			}
			ticketStatusEntity = ticketStatusOptional.get();
		} catch(IllegalArgumentException ex) {
			throw new EntityNotFoundException("ticketStatus", value, ticketStatusService.getAllTicketStatusesString());
		}
		
		return ticketStatusEntity;
	}
}
