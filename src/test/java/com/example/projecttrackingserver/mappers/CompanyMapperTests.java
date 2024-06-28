package com.example.projecttrackingserver.mappers;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.projecttrackingserver.TestDataUtil;
import com.example.projecttrackingserver.dto.CompanyRequestDto;
import com.example.projecttrackingserver.dto.CompanyResponseDto;
import com.example.projecttrackingserver.entities.CompanyEntity;

/**
 * Test class for CompanyMapper.
 * This class tests the if the mapping functionality work as intended.
 */
public class CompanyMapperTests {

	private CompanyMapper underTest;
	
	@BeforeEach
	public void setUp() {
		// Arrange
		underTest = new CompanyMapperImpl();
	}
	
    /**
     * Tests the toEntity method.
     * Expects correct mapping from dto to entity.
     */
	@Test
	public void toEntity_MapFromDtoToEntity_ReturnEntity() {
		// Arrange
		CompanyRequestDto companyRequestDto = new CompanyRequestDto("Testcompany", "Testdescription");
		
		// Act
		CompanyEntity companyEntity = underTest.toEntity(companyRequestDto);
		
		// Assert
		assertAll(() -> {
			assertEquals(0, companyEntity.getId());
			assertEquals(companyRequestDto.name(), companyEntity.getName());
			assertEquals(companyRequestDto.description(), companyEntity.getDescription());
			assertNull(companyEntity.getProjects());
		});
	}
	
    /**
     * Tests the toDto method.
     * Expects correct mapping from entity to dto.
     */
	@Test
	public void toDto_MapFromEntityToDto_ReturnDto() {
		// Arrange
		CompanyEntity testCompany = TestDataUtil.createCompany1();
		
		// Act
		CompanyResponseDto companyResponseDto = underTest.toDto(testCompany);

		// Assert
		assertAll(() -> {
			assertEquals(testCompany.getId(), companyResponseDto.id());
			assertEquals(testCompany.getName(), companyResponseDto.name());
			assertEquals(testCompany.getDescription(), companyResponseDto.description());
		});
	}
}
