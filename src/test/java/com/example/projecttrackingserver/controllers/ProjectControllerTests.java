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
import com.example.projecttrackingserver.dto.ProjectRequestDto;
import com.example.projecttrackingserver.dto.ProjectResponseDto;
import com.example.projecttrackingserver.dto.UserResponseDto;
import com.example.projecttrackingserver.enums.Role;
import com.example.projecttrackingserver.services.ProjectService;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Test class for ProjectController.
 * This class tests the endpoints of the ProjectController and ensures that they work as intended.
 */
@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = ProjectController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ProjectControllerTests {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private ProjectService projectService;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@MockBean
	private ApiKeyAuthExtractor apiKeyAuthExtractor;
	
	private long companyId;
	private long projectId;
	private ProjectRequestDto projectRequestDto;
	private ProjectResponseDto expectedResponse;
	@BeforeEach
	public void setUp() {
		// Arrange
		companyId = 1;
		projectId = 1;
		projectRequestDto = new ProjectRequestDto("TestProject", "TestDescription", "2024-05-23", "2024-06-18", 1L);
		expectedResponse = new ProjectResponseDto(projectId, projectRequestDto.projectName(), projectRequestDto.description(), LocalDate.parse(projectRequestDto.startDate()), LocalDate.parse(projectRequestDto.endDate()), projectRequestDto.projectManagerId());	
	}
	
    /**
     * Tests endpoint to retrieve all projects in a given company.
     * Expects successful response with multiple projects.
     */
	@Test
	public void GetAllProjectsInCompany_RetrieveMultipleProjects_ReturnMultipleProjectResponses() throws Exception {
		// Arrange
		ProjectResponseDto expectedResponse2 = new ProjectResponseDto(2, "TestProject2", "TestDescription", LocalDate.of(2023, 4, 11), LocalDate.of(2024, 2, 13), 1);
		List<ProjectResponseDto> expectedResponseList = Arrays.asList(expectedResponse, expectedResponse2);
		
		// Mock
		when(projectService.getAllProjectDtosInCompany(companyId))
						   .thenReturn(expectedResponseList);
		
		// Act
		ResultActions response = mockMvc.perform(get(String.format("/api/v1/companies/%d/projects", companyId))
										.contentType(MediaType.APPLICATION_JSON));

		// Assert
		response.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.[0].id").value(expectedResponseList.get(0).id()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.[0].projectName").value(expectedResponseList.get(0).projectName()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.[0].description").value(expectedResponseList.get(0).description()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.[0].startDate").value(expectedResponseList.get(0).startDate().toString()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.[0].endDate").value(expectedResponseList.get(0).endDate().toString()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.[0].projectManagerId").value(expectedResponseList.get(0).projectManagerId()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(expectedResponseList.size()));
	}
	
    /**
     * Tests endpoint to retrieve one project in given company.
     * Expects successful response with one project.
     */
	@Test
	public void GetOneProjectsInCompany_RetrieveOneProject_ReturnOneProjectResponse() throws Exception {
		// Mock
		when(projectService.getProjectDtoInCompany(companyId, projectId))
						   .thenReturn(expectedResponse);
		
		// Act
		ResultActions response = mockMvc.perform(get(String.format("/api/v1/companies/%d/projects/%d", companyId, projectId))
										.contentType(MediaType.APPLICATION_JSON));

		// Assert
		response.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(expectedResponse.id()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.projectName").value(expectedResponse.projectName()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.description").value(expectedResponse.description()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.startDate").value(expectedResponse.startDate().toString()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.endDate").value(expectedResponse.endDate().toString()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.projectManagerId").value(expectedResponse.projectManagerId()));
	}
	
    /**
     * Tests endpoint to retrieve all users in a given project.
     * Expects successful response with multiple users.
     */
	@Test
	public void GetAllUsersInProject_RetrieveMultipleUsers_ReturnMultipleProjectResponse() throws Exception {
		UserResponseDto userResponseDto1 = new UserResponseDto(1, "TestUser1", "TestCompany", Role.Developer, "1234");
		UserResponseDto userResponseDto2 = new UserResponseDto(2, "TestUser2", "TestCompany", Role.Developer, "5678");
		List<UserResponseDto> expectedResponseList = Arrays.asList(userResponseDto1, userResponseDto2);
		
		// Mock
		when(projectService.getAllUserDtosInProject(companyId, projectId))
						   .thenReturn(expectedResponseList);
		
		// Act
		ResultActions response = mockMvc.perform(get(String.format("/api/v1/companies/%d/projects/%d/users", companyId, projectId))
										.contentType(MediaType.APPLICATION_JSON));

		// Assert
		response.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.[0].id").value(expectedResponseList.get(0).id()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.[0].username").value(expectedResponseList.get(0).username()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.[0].company").value(expectedResponseList.get(0).company()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.[0].role").value(expectedResponseList.get(0).role().toString()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.[0].apiKey").value(expectedResponseList.get(0).apiKey().toString()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(expectedResponseList.size()));
	}
	
    /**
     * Tests endpoint to create a project.
     * Expects successful response with one project.
     */
	@Test
	public void CreateOneProject_CreateProject_ReturnOneProjectResponse() throws Exception {
		// Arrange
		String projectJson = objectMapper.writeValueAsString(projectRequestDto);
		
		// Mock
		when(projectService.createProject(projectRequestDto, companyId))
						   .thenReturn(expectedResponse);
		
		// Act
		ResultActions response = mockMvc.perform(post(String.format("/api/v1/companies/%d/projects", companyId))
										.contentType(MediaType.APPLICATION_JSON)
										.content(projectJson));

		// Assert
		response.andExpect(MockMvcResultMatchers.status().isCreated())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(expectedResponse.id()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.projectName").value(expectedResponse.projectName()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.description").value(expectedResponse.description()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.startDate").value(expectedResponse.startDate().toString()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.endDate").value(expectedResponse.endDate().toString()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.projectManagerId").value(expectedResponse.projectManagerId()));
	}
	
    /**
     * Tests endpoint to update a project.
     * Expects successful response with one project.
     */
	@Test
	public void UpdateOneProject_UpdateProjectFields_ReturnOneProjectResponse() throws Exception {
		// Arrange
		ProjectRequestDto projectRequestDto = new ProjectRequestDto("Updated TestProject", "Updated TestDescription", "2024-05-23", "2025-06-18", 1L);
		String projectJson = objectMapper.writeValueAsString(projectRequestDto);	
		ProjectResponseDto updatedResponseDto = new ProjectResponseDto(projectId, projectRequestDto.projectName(), projectRequestDto.description(), LocalDate.parse(projectRequestDto.startDate()), LocalDate.parse(projectRequestDto.endDate()), projectRequestDto.projectManagerId());
	    
	    // Mock
	    when(projectService.updateProject(projectRequestDto, companyId, projectId))
						   .thenReturn(updatedResponseDto);
	    
	    // Act
 		ResultActions response = mockMvc.perform(patch(String.format("/api/v1/companies/%d/projects/%d", companyId, projectId))
 										.contentType(MediaType.APPLICATION_JSON)
 										.content(projectJson));
 		
 		// Assert
		response.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(updatedResponseDto.id()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.projectName").value(updatedResponseDto.projectName()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.description").value(updatedResponseDto.description()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.startDate").value(updatedResponseDto.startDate().toString()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.endDate").value(updatedResponseDto.endDate().toString()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.projectManagerId").value(updatedResponseDto.projectManagerId()));
	}
	
    /**
     * Tests endpoint to remove a project.
     * Expects successful response with no body.
     */
	@Test
	public void DeleteOneProject_RemoveProject_ReturnNothing() throws Exception {
	    // Act
 		ResultActions response = mockMvc.perform(delete(String.format("/api/v1/companies/%d/projects/%d", companyId, projectId))
 										.contentType(MediaType.APPLICATION_JSON));
 		
 		// Assert
 		response.andExpect(MockMvcResultMatchers.status().isNoContent());
 		verify(projectService, times(1)).deleteProject(companyId, projectId);
	}
	
    /**
     * Tests endpoint to add a user to a project.
     * Expects successful response with no body.
     */
	@Test
	public void AddOneUserToProject_UserAdded_ReturnNothing() throws Exception {
		// Arrange
		long userIdToAdd = 123;
		
	    // Act
 		ResultActions response = mockMvc.perform(post(String.format("/api/v1/companies/%d/projects/%d/add-user/%d", companyId, projectId, userIdToAdd))
 										.contentType(MediaType.APPLICATION_JSON));
 		
 		// Assert
 		response.andExpect(MockMvcResultMatchers.status().isNoContent());
 		verify(projectService, times(1)).alterProjectMembersInProject(companyId, projectId, userIdToAdd, true);
	}
	
    /**
     * Tests endpoint to remove a user from a project.
     * Expects successful response with no body.
     */
	@Test
	public void RemoveOneUserFromProject_UserRemoved_ReturnNothing() throws Exception {
		// Arrange
		long userIdToRemove = 123;
		
	    // Act
 		ResultActions response = mockMvc.perform(delete(String.format("/api/v1/companies/%d/projects/%d/remove-user/%d", companyId, projectId, userIdToRemove))
 										.contentType(MediaType.APPLICATION_JSON));
 		
 		// Assert
 		response.andExpect(MockMvcResultMatchers.status().isNoContent());
 		verify(projectService, times(1)).alterProjectMembersInProject(companyId, projectId, userIdToRemove, false);
	}
}

