package com.example.projecttrackingserver.mappers;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.projecttrackingserver.TestDataUtil;
import com.example.projecttrackingserver.dto.ProjectRequestDto;
import com.example.projecttrackingserver.dto.ProjectResponseDto;
import com.example.projecttrackingserver.entities.CompanyEntity;
import com.example.projecttrackingserver.entities.ProjectEntity;
import com.example.projecttrackingserver.entities.RoleEntity;
import com.example.projecttrackingserver.entities.UserEntity;

/**
 * Test class for ProjectMapper.
 * This class tests the if the mapping functionality work as intended.
 */
public class ProjectMapperTests {

	private ProjectMapper underTest;
	
	private CompanyEntity companyEntity;
	private RoleEntity roleEntity;
	private UserEntity userEntity;	
	@BeforeEach
	public void setUp() {
		// Arrange
		underTest = new ProjectMapperImpl();
		companyEntity = TestDataUtil.createCompany1();
		roleEntity = TestDataUtil.createProjectManagerRole();
		userEntity = TestDataUtil.createUser1(roleEntity, companyEntity);
	}

    /**
     * Tests the toEntity method.
     * Expects correct mapping from dto to entity.
     */
	@Test
	public void toEntity_mapFromDtoToEntity_ReturnEntity() {
		// Arrange
		ProjectRequestDto projectRequestDto = new ProjectRequestDto("Testname", "Testdescription", "10-10-2001", "11-11-2011", 1L);
		
		// Act
		ProjectEntity projectEntity = underTest.toEntity(projectRequestDto, companyEntity, userEntity);
		
		// Assert
		assertAll(() -> {
			assertEquals(0, projectEntity.getId());
			assertEquals(projectRequestDto.projectName(), projectEntity.getName());
			assertEquals(projectRequestDto.description(), projectEntity.getDescription());
			assertEquals(LocalDate.of(2001, 10, 10), projectEntity.getStartDate());
			assertEquals(LocalDate.of(2011, 11, 11), projectEntity.getEndDate());
			assertEquals(userEntity, projectEntity.getProjectManager());
			assertEquals(companyEntity, projectEntity.getCompany());
			assertNull(projectEntity.getMembers());
			assertNull(projectEntity.getTickets());
		});
	}
	
    /**
     * Tests the toDto method.
     * Expects correct mapping from entity to dto.
     */
	@Test
	public void toDto_MapFromEntityToDto_ReturnDto() {
		// Arrange
		ProjectEntity projectEntity = TestDataUtil.createProject1(companyEntity, userEntity);
		
		// Act
		ProjectResponseDto projectResponseDto = underTest.toDto(projectEntity);

		// Assert
		assertAll(() -> {
			assertEquals(projectEntity.getId(), projectResponseDto.id());
			assertEquals(projectEntity.getName(), projectResponseDto.projectName());
			assertEquals(projectEntity.getDescription(), projectResponseDto.description());
			assertEquals(projectEntity.getStartDate(), projectResponseDto.startDate());
			assertEquals(projectEntity.getEndDate(), projectResponseDto.endDate());
			assertEquals(projectEntity.getProjectManager().getId(), projectResponseDto.projectManagerId());
		});
	}
	
    /**
     * Tests the update method.
     * Expects correct mapping and updating of entity.
     */
	@Test
	public void updatedEntity_UpdateEntityFields_ReturnEntity() {
		// Arrange
		ProjectEntity projectEntity = TestDataUtil.createProject1(companyEntity, userEntity);
		ProjectRequestDto projectRequestDto = new ProjectRequestDto("Updated Name", "Updated Description", null, null, 0l);
		
		// Act
		ProjectEntity updatedProject = underTest.updateEntity(projectEntity, projectRequestDto, userEntity);
		
		// Assert
		assertAll(() -> {
			assertEquals(projectEntity.getId(), updatedProject.getId());
			assertEquals(projectRequestDto.projectName(), updatedProject.getName());
			assertEquals(projectRequestDto.description(), updatedProject.getDescription());
			assertEquals(projectEntity.getStartDate(), updatedProject.getStartDate());
			assertEquals(projectEntity.getEndDate(), updatedProject.getEndDate());
			assertEquals(userEntity, updatedProject.getProjectManager());
			assertEquals(projectEntity.getMembers(), updatedProject.getMembers());
			assertEquals(projectEntity.getCompany(), updatedProject.getCompany());
			assertEquals(projectEntity.getTickets(), updatedProject.getTickets());
		});
	}
}
