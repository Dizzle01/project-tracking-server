package com.example.projecttrackingserver.controllers;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

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
import com.example.projecttrackingserver.dto.UserRequestDto;
import com.example.projecttrackingserver.dto.UserResponseDto;
import com.example.projecttrackingserver.enums.Role;
import com.example.projecttrackingserver.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Test class for UserController.
 * This class tests the endpoints of the UserController and ensures that they work as intended.
 */
@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTests {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private UserService userService;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@MockBean
	private ApiKeyAuthExtractor apiKeyAuthExtractor;
	
	private long companyId;
	private long userId;
	private UserRequestDto userRequestDto;
	private UserResponseDto expectedResponse;
	@BeforeEach
	public void setUp() {
		// Arrange
		companyId = 1;
		userId = 1;
		userRequestDto = new UserRequestDto("TestUser");
		expectedResponse = new UserResponseDto(userId, userRequestDto.username(), "TestCompany", Role.Developer, "1234");
	}
	
    /**
     * Tests endpoint to retrieve all users in a given company.
     * Expects successful response with multiple users.
     */
	@Test
	public void GetAllUsersInCompany_RetrieveMultipleUsers_ReturnMultipleUserResponses() throws Exception {
		// Arrange
		UserResponseDto expectedResponse2 = new UserResponseDto(2, "TestUser2", "TestCompany", Role.Developer, "5678");	
		List<UserResponseDto> expectedResponseList = Arrays.asList(expectedResponse, expectedResponse2);
		
		// Mock
		when(userService.getAllUserDtosByCompanyId(companyId))
						.thenReturn(expectedResponseList);
		
		// Act
		ResultActions response = mockMvc.perform(get(String.format("/api/v1/companies/%d/users", companyId))
										.contentType(MediaType.APPLICATION_JSON));

		// Assert
		response.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.[0].id").value(expectedResponseList.get(0).id()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.[0].username").value(expectedResponseList.get(0).username()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.[0].company").value(expectedResponseList.get(0).company()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.[0].role").value(expectedResponseList.get(0).role().toString()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.[0].apiKey").value(expectedResponseList.get(0).apiKey()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(expectedResponseList.size()));
	}
	
    /**
     * Tests endpoint to retrieve one user in a given company.
     * Expects successful response with one user.
     */
	@Test
	public void GetOneUserInCompany_RetrieveOneUser_ReturnOneUserResponse() throws Exception {
		// Mock
		when(userService.getUserDtoById(companyId, userId))
						.thenReturn(expectedResponse);
		
		// Act
		ResultActions response = mockMvc.perform(get(String.format("/api/v1/companies/%d/users/%d", companyId, userId))
										.contentType(MediaType.APPLICATION_JSON));
		
		// Assert
		response.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(expectedResponse.id()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.username").value(expectedResponse.username()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.company").value(expectedResponse.company()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.role").value(expectedResponse.role().toString()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.apiKey").value(expectedResponse.apiKey()));
	}
	
    /**
     * Tests endpoint to create one user.
     * Expects successful response with one user.
     */
	@Test
	public void CreateOneUser_CreateUser_ReturnOneUserResponse() throws Exception {
		// Arrange
		String userJson = objectMapper.writeValueAsString(userRequestDto);	
	    
		// Mock
	    when(userService.createUser(userRequestDto, userId))
	    				.thenReturn(expectedResponse);
		
		// Act
		ResultActions response = mockMvc.perform(post(String.format("/api/v1/companies/%d/users", companyId))
										.contentType(MediaType.APPLICATION_JSON)
										.content(userJson));
		
		// Assert
		response.andExpect(MockMvcResultMatchers.status().isCreated())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(expectedResponse.id()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.username").value(expectedResponse.username()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.company").value(expectedResponse.company()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.role").value(expectedResponse.role().toString()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.apiKey").value(expectedResponse.apiKey()));
	}
	
    /**
     * Tests endpoint to update one user.
     * Expects successful response with one user.
     */
	@Test
	public void UpdateOneUser_UpdateUserFields_ReturnOneUserResponse() throws Exception {
		// Arrange
		UserRequestDto userRequestDto = new UserRequestDto("Updated TestUser");
		String userJson = objectMapper.writeValueAsString(userRequestDto);	
	    UserResponseDto updatedResponseDto = new UserResponseDto(userId, userRequestDto.username(), "TestCompany", Role.Developer, "1234");
	    
	    // Mock
	    when(userService.updateUser(userRequestDto, companyId, userId))
						.thenReturn(updatedResponseDto);
	    
	    // Act
 		ResultActions response = mockMvc.perform(patch(String.format("/api/v1/companies/%d/users/%d", companyId, userId))
 										.contentType(MediaType.APPLICATION_JSON)
 										.content(userJson));
 		
 		// Assert
 		response.andExpect(MockMvcResultMatchers.status().isOk())
 				.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(updatedResponseDto.id()))
 				.andExpect(MockMvcResultMatchers.jsonPath("$.username").value(updatedResponseDto.username()))
 				.andExpect(MockMvcResultMatchers.jsonPath("$.company").value(updatedResponseDto.company()))
 				.andExpect(MockMvcResultMatchers.jsonPath("$.role").value(updatedResponseDto.role().toString()))
 				.andExpect(MockMvcResultMatchers.jsonPath("$.apiKey").value(updatedResponseDto.apiKey()));
	}
	
    /**
     * Tests endpoint to assign a new role to one user.
     * Expects successful response with one user.
     */
	@Test
	public void AssignRoleToUser_GiveUserNewRole_ReturnOneUserResponse() throws Exception {
		// Arrange
		long roleId = 1;
		
		// Mock
	    when(userService.assignRoleToUser(companyId, userId, roleId))
						.thenReturn(expectedResponse);
	    
	    // Act
 		ResultActions response = mockMvc.perform(patch(String.format("/api/v1/companies/%d/users/%d/assign-role/%d", companyId, userId, roleId))
 										.contentType(MediaType.APPLICATION_JSON));
 		
 		// Assert
 		response.andExpect(MockMvcResultMatchers.status().isOk())
 				.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(expectedResponse.id()))
 				.andExpect(MockMvcResultMatchers.jsonPath("$.username").value(expectedResponse.username()))
 				.andExpect(MockMvcResultMatchers.jsonPath("$.company").value(expectedResponse.company()))
 				.andExpect(MockMvcResultMatchers.jsonPath("$.role").value(expectedResponse.role().toString()))
 				.andExpect(MockMvcResultMatchers.jsonPath("$.apiKey").value(expectedResponse.apiKey()));
	}
	
    /**
     * Tests endpoint to remove one user.
     * Expects successful response with no body.
     */
	@Test
	public void DeleteOneUser_RemoveUser_ReturnNothing() throws Exception {
	    // Act
 		ResultActions response = mockMvc.perform(delete(String.format("/api/v1/companies/%d/users/%d", companyId, userId))
 										.contentType(MediaType.APPLICATION_JSON));
 		
 		// Assert
 		response.andExpect(MockMvcResultMatchers.status().isNoContent());
 		verify(userService, times(1)).deleteUser(companyId, userId);
	}
}
