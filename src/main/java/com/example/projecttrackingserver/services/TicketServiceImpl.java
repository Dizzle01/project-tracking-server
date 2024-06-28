package com.example.projecttrackingserver.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.projecttrackingserver.dto.TicketRequestDto;
import com.example.projecttrackingserver.dto.TicketResponseDto;
import com.example.projecttrackingserver.entities.ProjectEntity;
import com.example.projecttrackingserver.entities.TicketEntity;
import com.example.projecttrackingserver.entities.UserEntity;
import com.example.projecttrackingserver.exceptions.EntityAlreadyExistsException;
import com.example.projecttrackingserver.exceptions.EntityNotFoundException;
import com.example.projecttrackingserver.exceptions.UnauthorizedException;
import com.example.projecttrackingserver.mappers.TicketMapper;
import com.example.projecttrackingserver.repositories.TicketRepository;

import lombok.RequiredArgsConstructor;

/**
 * Implementation of {@link TicketService} interface 
 */
@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

	private final TicketRepository ticketRepository;
	private final TicketMapper ticketMapper;
	private final ProjectService projectService;
	private final CompanyService companyService;
	
    /**
     * {@inheritDoc}
     */
    public List<TicketResponseDto> getAllTicketDtosInProject(long companyId, long projectId) {
    	// company does not exist -> deny
		if(!companyService.entityExists(companyId)) {
			throw new EntityNotFoundException("companyId", companyId);
		}
		
		// project does not exist -> deny
		Optional<ProjectEntity> projectOptional = projectService.getEntityByIdAndCompanyId(projectId, companyId);
		if(projectOptional.isEmpty()) {
			throw new EntityNotFoundException("projectId", projectId);
		}

		return StreamSupport.stream(projectOptional.get().getTickets().spliterator(), false)
							.map(ticket -> ticketMapper.toDto(ticket))
							.collect(Collectors.toList());
    }
	
    /**
     * {@inheritDoc}
     */
	public TicketResponseDto getTicketDtoById(long companyId, long projectId, long ticketId) {
		// company does not exist -> deny
		if(!companyService.entityExists(companyId)) {
			throw new EntityNotFoundException("companyId", companyId);
		}
		
		// project does not exist -> deny
		if(!projectService.entityExists(projectId, companyId)) {
			throw new EntityNotFoundException("projectId", projectId);
		}
		
		// ticket does not exist -> deny
		Optional<TicketEntity> ticketOptional = ticketRepository.findByIdAndProjectId(ticketId, projectId);
		if(ticketOptional.isEmpty()) {
			throw new EntityNotFoundException("ticketId", ticketId);
		}
			
		return ticketMapper.toDto(ticketOptional.get());
	}
	
    /**
     * {@inheritDoc}
     */
	public TicketResponseDto createTicket(TicketRequestDto ticketRequestDto, long companyId, long projectId) {
		// company does not exist -> deny
		if(!companyService.entityExists(companyId)) {
			throw new EntityNotFoundException("companyId", companyId);
		}
		
		// project does not exists -> deny
		Optional<ProjectEntity> projectOptional = projectService.getEntityByIdAndCompanyId(projectId, companyId);
		if(projectOptional.isEmpty()) {
			throw new EntityNotFoundException("projectId", projectId);
		}
		ProjectEntity projectEntity = projectOptional.get();
		
		// requesting user is not in project -> deny
		UserEntity requestingUser = (UserEntity)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(!projectEntity.getMembers().stream().anyMatch(user -> user.getId() == requestingUser.getId())) {
			throw new UnauthorizedException();
		}

		// ticket with same name already exists -> deny
		if(ticketExists(ticketRequestDto.name(), projectId)) {
			throw new EntityAlreadyExistsException("name", ticketRequestDto.name());
		}
		
		TicketEntity ticketEntity = ticketMapper.toEntity(ticketRequestDto, projectEntity, requestingUser);
		
		ticketEntity = ticketRepository.save(ticketEntity);
		
		return ticketMapper.toDto(ticketEntity);
	}
	
    /**
     * {@inheritDoc}
     */
	public TicketResponseDto updateTicket(TicketRequestDto ticketRequestDto, long companyId, long projectId, long ticketId) {
		// company does not exist -> deny
		if(!companyService.entityExists(companyId)) {
			throw new EntityNotFoundException("companyId", companyId);
		}
		
		// project does not exist -> deny
		if(!projectService.entityExists(projectId, companyId)) {
			throw new EntityNotFoundException("projectId", projectId);
		}
		
		// ticket does not exist -> deny
		Optional<TicketEntity> ticketOptional = getEntityByIdAndProjectId(ticketId, projectId);
		if(ticketOptional.isEmpty()) {
			throw new EntityNotFoundException("ticketId", ticketId);
		}
		TicketEntity ticketToUpdateEnitity = ticketOptional.get();
		
		// requesting user is not creator of ticket or project manager -> deny
		UserEntity requestingUser = (UserEntity)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(!(requestingUser.getId() == ticketToUpdateEnitity.getCreator().getId() || requestingUser.getId() == ticketToUpdateEnitity.getProject().getProjectManager().getId())) {
			throw new UnauthorizedException();
		}
		
		// ticket with same name already exists -> deny
		if(ticketExists(ticketRequestDto.name(), projectId)) {
			throw new EntityAlreadyExistsException("name", ticketRequestDto.name());
		}
		
		ticketToUpdateEnitity = ticketMapper.updateEntity(ticketToUpdateEnitity, ticketRequestDto);
		
		ticketToUpdateEnitity = ticketRepository.save(ticketToUpdateEnitity);

		return ticketMapper.toDto(ticketToUpdateEnitity);
	}
	
    /**
     * {@inheritDoc}
     */
	public void deleteTicket(long companyId, long projectId, long ticketId) {
		// company does not exist -> deny
		if(!companyService.entityExists(companyId)) {
			throw new EntityNotFoundException("companyId", companyId);
		}
		
		// project does not exist -> deny
		Optional<ProjectEntity> projectOptional = projectService.getEntityByIdAndCompanyId(projectId, companyId);
		if(projectOptional.isEmpty()) {
			throw new EntityNotFoundException("projectId", projectId);
		}
		ProjectEntity projectEntity = projectOptional.get();
		
		// ticket does not exist -> deny
		Optional<TicketEntity> ticketOptional = ticketRepository.findByIdAndProjectId(ticketId, projectId);
		if(ticketOptional.isEmpty()) {
			throw new EntityNotFoundException("ticketId", ticketId);
		}
		TicketEntity ticketEntityToDelete = ticketOptional.get();
		
		// requesting user is not project manager of same project as ticket -> deny
		UserEntity requestingUser = (UserEntity)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(projectEntity.getProjectManager().getId() != requestingUser.getId()) {
			throw new UnauthorizedException();
		}
		
		ticketRepository.delete(ticketEntityToDelete);
	}
	
    /**
     * {@inheritDoc}
     */
	public boolean ticketExists(String name, long projectId) {
		return ticketRepository.findByNameAndProjectId(name, projectId).isPresent();
	}
	
    /**
     * {@inheritDoc}
     */
	public Optional<TicketEntity> getEntityByIdAndProjectId(long ticketId, long projectId) {
		return ticketRepository.findByIdAndProjectId(ticketId, projectId);
	}
}
