package com.example.projecttrackingserver.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.projecttrackingserver.dto.TicketRequestDto;
import com.example.projecttrackingserver.dto.TicketResponseDto;
import com.example.projecttrackingserver.services.TicketService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Controller class for handling ticket-related HTTP requests.
 */
@RestController
@RequestMapping(path = "/api/v1/companies/{companyId}/projects/{projectId}/tickets")
@RequiredArgsConstructor
public class TicketController {

	private final TicketService ticketService;
	
    /**
     * Endpoint to retrieve all tickets in a specific project.
     *
     * @param companyId ID of the company that owns the project
     * @param projectId ID of the project to retrieve tickets for
     * @return ResponseEntity containing a list of TicketResponseDto and HTTP status code OK (200)
     */
	@GetMapping
	public ResponseEntity<List<TicketResponseDto>> getAllTicketsInProject(
			@PathVariable long companyId,
			@PathVariable long projectId
	) {
		return new ResponseEntity<List<TicketResponseDto>>(
				ticketService.getAllTicketDtosInProject(companyId, projectId),
				HttpStatus.OK);
	}
	
    /**
     * Endpoint to retrieve a specific ticket in a project by its ID.
     *
     * @param companyId ID of the company that owns the project
     * @param projectId ID of the project that contains the ticket
     * @param ticketId ID of the ticket to retrieve
     * @return ResponseEntity containing the TicketResponseDto and HTTP status code OK (200)
     */
	@GetMapping(path = "/{ticketId}")
	public ResponseEntity<TicketResponseDto> getOneTicketInProject(
			@PathVariable long companyId,
			@PathVariable long projectId,
			@PathVariable long ticketId
	) {
		return new ResponseEntity<TicketResponseDto>(
				ticketService.getTicketDtoById(companyId, projectId, ticketId),
				HttpStatus.OK);
	}
	
    /**
     * Endpoint to create a new ticket in a project.
     *
     * @param ticketRequestDto The details of the ticket to be created
     * @param companyId ID of the company that owns the project
     * @param projectId ID of the project where the ticket will be created
     * @return ResponseEntity containing the TicketResponseDto of the created ticket and HTTP status code CREATED (201)
     */
	@PostMapping
	public ResponseEntity<TicketResponseDto> createOneTicket(
			@Valid @RequestBody TicketRequestDto ticketRequestDto,
			@PathVariable long companyId,
			@PathVariable long projectId
	) {
		return new ResponseEntity<TicketResponseDto>(
				ticketService.createTicket(ticketRequestDto, companyId, projectId),
				HttpStatus.CREATED);
	}
	
    /**
     * Endpoint to delete a ticket from a project.
     *
     * @param companyId ID of the company that owns the project
     * @param projectId ID of the project that contains the ticket
     * @param ticketId ID of the ticket to be deleted
     * @return ResponseEntity with HTTP status code NO CONTENT indicating successful deletion
     */
	@DeleteMapping(path = "/{ticketId}")
	public ResponseEntity<Void> deleteOneTicket(
			@PathVariable long companyId,
			@PathVariable long projectId,
			@PathVariable long ticketId
	) {
		ticketService.deleteTicket(companyId, projectId, ticketId);
		return ResponseEntity.noContent().build();
	}
	
    /**
     * Endpoint to update a ticket in a project.
     *
     * @param ticketRequestDto The updated details of the ticket
     * @param companyId ID of the company that owns the project
     * @param projectId ID of the project that contains the ticket
     * @param ticketId ID of the ticket to be updated
     * @return ResponseEntity containing the TicketResponseDto of the updated ticket and HTTP status code OK (200)
     */
	@PatchMapping(path = "/{ticketId}")
	public ResponseEntity<TicketResponseDto> updateOneTicket(
			@RequestBody TicketRequestDto ticketRequestDto,
			@PathVariable long companyId,
			@PathVariable long projectId,
			@PathVariable long ticketId
	) {
		return new ResponseEntity<TicketResponseDto>(
				ticketService.updateTicket(ticketRequestDto, companyId, projectId, ticketId),
				HttpStatus.OK);
	}
}
