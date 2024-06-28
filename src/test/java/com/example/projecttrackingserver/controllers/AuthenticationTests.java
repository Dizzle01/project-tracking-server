package com.example.projecttrackingserver.controllers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.example.projecttrackingserver.TestDataUtil;
import com.example.projecttrackingserver.entities.CompanyEntity;
import com.example.projecttrackingserver.entities.RoleEntity;
import com.example.projecttrackingserver.entities.UserEntity;
import com.example.projecttrackingserver.services.UserServiceImpl;

/**
 * Test class for Authenticating with an ApiKey.
 * This class tests if endpoints are reacting accordingly to Authentication.
 */
@SpringBootTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
public class AuthenticationTests {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private UserServiceImpl userService;
	
	private UserEntity requestingUser;
	private long companyId;
	private long userId;
	@BeforeEach
	public void setUp() {
		RoleEntity adminRoleEntity = TestDataUtil.createAdminRole();
		CompanyEntity companyEntity = TestDataUtil.createCompany1();
		requestingUser = TestDataUtil.createUser1(adminRoleEntity, companyEntity);
		companyId = companyEntity.getId();
		userId = requestingUser.getId();
	}
	
    /**
     * Tests ApiKey secured endpoint.
     * Expects access to resource and successful response.
     */
	@Test
	public void Extract_CheckAuthentication_Successful() throws Exception {
		// Arrange
		String apiKey = requestingUser.getApiKey();
		
		// Mock
		when(userService.getEntityByAPIKey(apiKey))
						.thenReturn(Optional.of(requestingUser));
		
		// Act
		ResultActions response = mockMvc.perform(delete(String.format("/api/v1/companies/%d/users/%d", companyId, userId))
										.contentType(MediaType.APPLICATION_JSON)
										.header("ApiKey", apiKey));
		
		// Assert
		response.andExpect(MockMvcResultMatchers.status().isNoContent());
	}
	
    /**
     * Tests ApiKey secured endpoint.
     * Expects failed access to resource and unauthorized response.
     */
	@Test
	public void Extract_CheckAuthentication_Fail() throws Exception {
		// Arrange
		String notValidApiKey = "7c3c8688c64d7211a5a03da751c9b388b68724f6ccc23e664b68887910d4de1a6";
		
		// Mock
		when(userService.getEntityByAPIKey(notValidApiKey))
						.thenReturn(Optional.empty());
		
		// Act
		ResultActions response = mockMvc.perform(delete(String.format("/api/v1/companies/%d/users/%d", companyId, userId))
										.contentType(MediaType.APPLICATION_JSON)
										.header("ApiKey", notValidApiKey));
		
		// Assert
		response.andExpect(MockMvcResultMatchers.status().isUnauthorized());
	}
}
