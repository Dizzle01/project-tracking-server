package com.example.projecttrackingserver.controllers;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.example.projecttrackingserver.auth.ApiKeyAuthExtractor;
import com.example.projecttrackingserver.dto.TicketRequestDto;
import com.example.projecttrackingserver.dto.TicketResponseDto;
import com.example.projecttrackingserver.enums.TicketPriority;
import com.example.projecttrackingserver.enums.TicketStatus;
import com.example.projecttrackingserver.enums.TicketType;
import com.example.projecttrackingserver.services.TicketService;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Test class for TicketController.
 * This class tests the endpoints of the TicketController and ensures that they work as intended.
 */
@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = TicketController.class)
@AutoConfigureMockMvc(addFilters = false)
public class TicketControllerTests {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private TicketService ticketService;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@MockBean
	private ApiKeyAuthExtractor apiKeyAuthExtractor;
	
	private long companyId;
	private long projectId;
	private long ticketId;
	private TicketRequestDto ticketRequestDto;
	private TicketResponseDto expectedResponse;
	@BeforeEach
	public void setUp() {
		// Arrange
		companyId = 1;
		projectId = 1;
		ticketId = 1;
		ticketRequestDto = new TicketRequestDto("TestTicket", "TestDescription", "Bug", "High", "New");
		expectedResponse = new TicketResponseDto(ticketId, ticketRequestDto.name(), ticketRequestDto.description(), LocalDate.parse("2024-07-12"), LocalDate.parse("2024-07-13"), projectId, 1L, TicketType.valueOf(ticketRequestDto.ticketType()), TicketPriority.valueOf(ticketRequestDto.ticketPriority()), TicketStatus.valueOf(ticketRequestDto.ticketStatus()));	
	}
	
    /**
     * Tests endpoint to retrieve all tickets in a given project.
     * Expects successful response with multiple tickets.
     */
	@Test
	public void GetAllTicketsInProject_RetrieveMultipleTickets_ReturnMultipleTicketResponses() throws Exception {
		// Arrange
		TicketResponseDto expectedResponse2 = new TicketResponseDto(2, "TestTicket2", ticketRequestDto.description(), LocalDate.parse("2024-07-12"), LocalDate.parse("2024-07-13"), projectId, 1L, TicketType.valueOf(ticketRequestDto.ticketType()), TicketPriority.valueOf(ticketRequestDto.ticketPriority()), TicketStatus.valueOf(ticketRequestDto.ticketStatus()));
		List<TicketResponseDto> expectedResponseList = Arrays.asList(expectedResponse, expectedResponse2);
		
		// Mock
		when(ticketService.getAllTicketDtosInProject(companyId, projectId))
						  .thenReturn(expectedResponseList);
		
		// Act
		ResultActions response = mockMvc.perform(get(String.format("/api/v1/companies/%d/projects/%d/tickets", companyId, projectId))
										.contentType(MediaType.APPLICATION_JSON));

		// Assert
		response.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.[0].id").value(expectedResponseList.get(0).id()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.[0].name").value(expectedResponseList.get(0).name()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.[0].description").value(expectedResponseList.get(0).description()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.[0].createdAt").value(expectedResponseList.get(0).createdAt().toString()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.[0].updatedAt").value(expectedResponseList.get(0).updatedAt().toString()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.[0].projectId").value(expectedResponseList.get(0).projectId()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.[0].creatorId").value(expectedResponseList.get(0).creatorId()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.[0].ticketType").value(expectedResponseList.get(0).ticketType().toString()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.[0].ticketPriority").value(expectedResponseList.get(0).ticketPriority().toString()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.[0].ticketStatus").value(expectedResponseList.get(0).ticketStatus().toString()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(expectedResponseList.size()));
	}
	
    /**
     * Tests endpoint to retrieve one ticket in a given project.
     * Expects successful response with one ticket.
     */
	@Test
	public void GetOneTicketInProject_RetrieveOneTicket_ReturnOneTicketResponse() throws Exception {
		// Mock
		when(ticketService.getTicketDtoById(companyId, projectId, ticketId))
						  .thenReturn(expectedResponse);
		
		// Act
		ResultActions response = mockMvc.perform(get(String.format("/api/v1/companies/%d/projects/%d/tickets/%d", companyId, projectId, ticketId))
										.contentType(MediaType.APPLICATION_JSON));

		// Assert
		response.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(expectedResponse.id()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.name").value(expectedResponse.name()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.description").value(expectedResponse.description()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.createdAt").value(expectedResponse.createdAt().toString()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.updatedAt").value(expectedResponse.updatedAt().toString()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.projectId").value(expectedResponse.projectId()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.creatorId").value(expectedResponse.creatorId()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.ticketType").value(expectedResponse.ticketType().toString()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.ticketPriority").value(expectedResponse.ticketPriority().toString()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.ticketStatus").value(expectedResponse.ticketStatus().toString()));
	}
	
    /**
     * Tests endpoint to create one ticket.
     * Expects successful response with one ticket.
     */
	@Test
	public void CreateOneTicket_CreateTicket_ReturnOneTicketResponse() throws Exception {
		// Arrange
		String ticketJson = objectMapper.writeValueAsString(ticketRequestDto);	
		
		// Mock
		when(ticketService.createTicket(ticketRequestDto, companyId, projectId))
						  .thenReturn(expectedResponse);
		
		// Act
		ResultActions response = mockMvc.perform(post(String.format("/api/v1/companies/%d/projects/%d/tickets", companyId, projectId))
										.contentType(MediaType.APPLICATION_JSON)
										.content(ticketJson));

		// Assert
		response.andExpect(MockMvcResultMatchers.status().isCreated())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(expectedResponse.id()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.name").value(expectedResponse.name()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.description").value(expectedResponse.description()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.createdAt").value(expectedResponse.createdAt().toString()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.updatedAt").value(expectedResponse.updatedAt().toString()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.projectId").value(expectedResponse.projectId()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.creatorId").value(expectedResponse.creatorId()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.ticketType").value(expectedResponse.ticketType().toString()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.ticketPriority").value(expectedResponse.ticketPriority().toString()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.ticketStatus").value(expectedResponse.ticketStatus().toString()));
	}
	
    /**
     * Tests endpoint to delete one ticket.
     * Expects successful response with no body.
     */
	@Test
	public void DeleteOneTicket_RemoveTicket_ReturnNothing() throws Exception {
	    // Act
 		ResultActions response = mockMvc.perform(delete(String.format("/api/v1/companies/%d/projects/%d/tickets/%d", companyId, projectId, ticketId))
 										.contentType(MediaType.APPLICATION_JSON));
 		
 		// Assert
 		response.andExpect(MockMvcResultMatchers.status().isNoContent());
 		verify(ticketService, times(1)).deleteTicket(companyId, projectId, ticketId);
	}
	
    /**
     * Tests endpoint to update one ticket.
     * Expects successful response with one ticket.
     */
	@Test
	public void UpdateOneTicket_UpdateTicketFields_ReturnOneTicketResponse() throws Exception {
		// Arrange
		TicketRequestDto ticketRequestDto = new TicketRequestDto("Updated TestTicket", "Updated TestDescription", "Bug", "High", "New");
		String ticketJson = objectMapper.writeValueAsString(ticketRequestDto);	
		TicketResponseDto updatedResponseDto = new TicketResponseDto(ticketId, ticketRequestDto.name(), ticketRequestDto.description(), LocalDate.parse("2024-07-12"), LocalDate.parse("2024-07-13"), projectId, 1L, TicketType.valueOf(ticketRequestDto.ticketType()), TicketPriority.valueOf(ticketRequestDto.ticketPriority()), TicketStatus.valueOf(ticketRequestDto.ticketStatus()));
	    
	    // Mock
	    when(ticketService.updateTicket(ticketRequestDto, companyId, projectId, ticketId))
						  .thenReturn(updatedResponseDto);
	    
	    // Act
 		ResultActions response = mockMvc.perform(patch(String.format("/api/v1/companies/%d/projects/%d/tickets/%d", companyId, projectId, ticketId))
 										.contentType(MediaType.APPLICATION_JSON)
 										.content(ticketJson));
 		
		// Assert
		response.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(updatedResponseDto.id()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.name").value(updatedResponseDto.name()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.description").value(updatedResponseDto.description()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.createdAt").value(updatedResponseDto.createdAt().toString()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.updatedAt").value(updatedResponseDto.updatedAt().toString()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.projectId").value(updatedResponseDto.projectId()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.creatorId").value(updatedResponseDto.creatorId()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.ticketType").value(updatedResponseDto.ticketType().toString()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.ticketPriority").value(updatedResponseDto.ticketPriority().toString()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.ticketStatus").value(updatedResponseDto.ticketStatus().toString()));
	}
}
