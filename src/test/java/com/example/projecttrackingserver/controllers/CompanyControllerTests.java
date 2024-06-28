package com.example.projecttrackingserver.controllers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.util.Arrays;
import java.util.List;

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
import com.example.projecttrackingserver.dto.CompanyResponseDto;
import com.example.projecttrackingserver.services.CompanyService;

/**
 * Test class for CompanyController.
 * This class tests the endpoints of the CompanyController and ensures that they work as intended.
 */
@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = CompanyController.class)
@AutoConfigureMockMvc(addFilters = false)
public class CompanyControllerTests {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private CompanyService companyService;
	
	@MockBean
	private ApiKeyAuthExtractor apiKeyAuthExtractor;
	
    /**
     * Tests endpoint to retrieve all companies.
     * Expects successful response with multiple companies.
     */
	@Test
	public void GetAllCompanies_RetrieveMultipleCompanies_ReturnMultipleCompanyResponses() throws Exception {
		// Arrange
		CompanyResponseDto companyResponseDto1 = new CompanyResponseDto(1, "TestCompany1", "TestDescription");
		CompanyResponseDto companyResponseDto2 = new CompanyResponseDto(2, "TestCompany2", "TestDescription");
		List<CompanyResponseDto> expectedResponse = Arrays.asList(companyResponseDto1, companyResponseDto2);
		
		// Mock
		when(companyService.getAllCompanyDtos())
		   				   .thenReturn(expectedResponse);

		// Act
		ResultActions response = mockMvc.perform(get("/api/v1/companies")
										.contentType(MediaType.APPLICATION_JSON));	
		
		// Assert
		response.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.[0].id").value(expectedResponse.get(0).id()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.[0].name").value(expectedResponse.get(0).name()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.[0].description").value(expectedResponse.get(0).description()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(expectedResponse.size()));
	}
	
    /**
     * Tests endpoint to retrieve one company.
     * Expects successful response with one company.
     */
	@Test
	public void GetOneCompany_RetrieveOneCompany_ReturnOneCompanyResponse() throws Exception {
		// Arrange
		long companyId = 1;
		CompanyResponseDto expectedResponse = new CompanyResponseDto(companyId, "TestCompany", "TestDescription");
		
		// Mock
		when(companyService.getCompanyDtoById(companyId))
						   .thenReturn(expectedResponse);
		
		// Act
		ResultActions response = mockMvc.perform(get(String.format("/api/v1/companies/%d", companyId))
										.contentType(MediaType.APPLICATION_JSON));

		// Assert
		response.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(expectedResponse.id()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.name").value(expectedResponse.name()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.description").value(expectedResponse.description()));
	}
}
