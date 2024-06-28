package com.example.projecttrackingserver.mappers;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.projecttrackingserver.TestDataUtil;
import com.example.projecttrackingserver.dto.UserRequestDto;
import com.example.projecttrackingserver.dto.UserResponseDto;
import com.example.projecttrackingserver.entities.CompanyEntity;
import com.example.projecttrackingserver.entities.RoleEntity;
import com.example.projecttrackingserver.entities.UserEntity;

/**
 * Test class for UserMapper.
 * This class tests the if the mapping functionality work as intended.
 */
public class UserMapperTests {

	private UserMapper underTest;
	
	private CompanyEntity companyEntity;
	private RoleEntity developerRoleEntity;
	@BeforeEach
	public void setUp() {
		// Arrange
		underTest = new UserMapperImpl();
		companyEntity = TestDataUtil.createCompany1();
		developerRoleEntity = TestDataUtil.createDeveloperRole();
	}
	
    /**
     * Tests the toEntity method.
     * Expects correct mapping from dto to entity.
     */
	@Test
	public void toEntity_mapFromDtoToEntity_ReturnEntity() {
		// Arrange
		UserRequestDto userRequestDto = new UserRequestDto("TestUser");
		
		// Act
		UserEntity userEntity = underTest.toEntity(userRequestDto, companyEntity, developerRoleEntity);
		
		// Assert
		assertAll(() -> {
			assertEquals(0, userEntity.getId());
			assertEquals(userRequestDto.username(), userEntity.getUsername());
			assertEquals(64, userEntity.getApiKey().length());
			assertEquals(LocalDate.now(), userEntity.getCreatedAt());
			assertEquals(companyEntity, userEntity.getCompany());
			assertEquals(developerRoleEntity, userEntity.getRole());
			assertNull(userEntity.getProjects());
		});
	}
	
    /**
     * Tests the toDto method.
     * Expects correct mapping from entity to dto.
     */
	@Test
	public void toDto_MapFromEntityToDto_ReturnDto() {
		// Arrange
		UserEntity userEntity = TestDataUtil.createUser1(developerRoleEntity, companyEntity);	
		
		// Act
		UserResponseDto userResponseDto = underTest.toDto(userEntity);
		
		// Assert
		assertAll(() -> {
			assertEquals(userEntity.getId(), userResponseDto.id());
			assertEquals(userEntity.getUsername(), userResponseDto.username());
			assertEquals(userEntity.getCompany().getName(), userResponseDto.company());
			assertEquals(userEntity.getRole().getRole(), userResponseDto.role());
			assertEquals(userEntity.getApiKey(), userResponseDto.apiKey());
		});
	}
	
    /**
     * Tests the update method.
     * Expects correct mapping and updating of entity.
     */
	@Test
	public void updatedEntity_UpdateEntityFields_ReturnEntity() {
		// Arrange
		UserRequestDto userRequestDto = new UserRequestDto("UpdatedName");
		UserEntity testUser = TestDataUtil.createUser1(developerRoleEntity, companyEntity);
		
		// Act
		UserEntity updatedUser = underTest.updateEntity(testUser, userRequestDto);
		
		// Assert
		assertAll(() -> {
			assertEquals(testUser.getId(), updatedUser.getId());
			assertEquals(userRequestDto.username(), updatedUser.getUsername());
			assertEquals(testUser.getApiKey(), updatedUser.getApiKey());
			assertEquals(testUser.getCreatedAt(), updatedUser.getCreatedAt());
			assertEquals(testUser.getCompany(), updatedUser.getCompany());
			assertEquals(testUser.getRole(), updatedUser.getRole());
			assertEquals(testUser.getProjects(), updatedUser.getProjects());
		});
	}
}
