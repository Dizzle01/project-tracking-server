package com.example.projecttrackingserver.mappers;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.projecttrackingserver.TestDataUtil;
import com.example.projecttrackingserver.dto.TicketRequestDto;
import com.example.projecttrackingserver.dto.TicketResponseDto;
import com.example.projecttrackingserver.entities.CompanyEntity;
import com.example.projecttrackingserver.entities.ProjectEntity;
import com.example.projecttrackingserver.entities.RoleEntity;
import com.example.projecttrackingserver.entities.TicketEntity;
import com.example.projecttrackingserver.entities.TicketPriorityEntity;
import com.example.projecttrackingserver.entities.TicketStatusEntity;
import com.example.projecttrackingserver.entities.TicketTypeEntity;
import com.example.projecttrackingserver.entities.UserEntity;
import com.example.projecttrackingserver.enums.TicketPriority;
import com.example.projecttrackingserver.enums.TicketStatus;
import com.example.projecttrackingserver.enums.TicketType;
import com.example.projecttrackingserver.services.TicketPriorityServiceImpl;
import com.example.projecttrackingserver.services.TicketStatusServiceImpl;
import com.example.projecttrackingserver.services.TicketTypeServiceImpl;

/**
 * Test class for TicketMapper.
 * This class tests the if the mapping functionality work as intended.
 */
@ExtendWith(MockitoExtension.class)
public class TicketMapperTests {

	@InjectMocks
	private TicketMapperImpl underTest = new TicketMapperImpl();
	
	@Mock
	private TicketTypeServiceImpl ticketTypeService;
	
	@Mock
	private TicketPriorityServiceImpl ticketPriorityService;
	
	@Mock
	private TicketStatusServiceImpl ticketStatusService;
	
	private CompanyEntity companyEntity;
	private RoleEntity projectManagerRoleEntity;
	private UserEntity projectManagerEntity;
	private ProjectEntity projectEntity;
	@BeforeEach
	public void setUp() {
		// Arrange
		companyEntity = TestDataUtil.createCompany1();
		projectManagerRoleEntity = TestDataUtil.createProjectManagerRole();
		projectManagerEntity = TestDataUtil.createUser1(projectManagerRoleEntity, companyEntity);
		projectEntity = TestDataUtil.createProject1(companyEntity, projectManagerEntity);
	}
	
    /**
     * Tests the toEntity method.
     * Expects correct mapping from dto to entity.
     */
	@Test
	public void toEntity_mapFromDtoToEntity_ReturnEntity() {
		// Arrange
		TicketType ticketType = TicketType.Bug;
		TicketPriority ticketPriority = TicketPriority.High;
		TicketStatus ticketStatus = TicketStatus.New;
		TicketRequestDto ticketRequestDto = new TicketRequestDto("TestName", "TestDescritpion", ticketType.toString(), ticketPriority.toString(), ticketStatus.toString());
		
		// Mock calls
		when(ticketTypeService.getEntityByTicketType(ticketType))
							  .thenReturn(Optional.of(TestDataUtil.createTestTicketType(ticketType)));
		when(ticketPriorityService.getEntityByTicketPriority(ticketPriority))
							  	  .thenReturn(Optional.of(TestDataUtil.createTestTicketPriority(ticketPriority)));
		when(ticketStatusService.getEntityByTicketStatus(ticketStatus))
		  						.thenReturn(Optional.of(TestDataUtil.createTestTicketStatus(ticketStatus)));
		
		// Act
		TicketEntity ticketEntity = underTest.toEntity(ticketRequestDto, projectEntity, projectManagerEntity);
		
		// Assert
		assertAll(() -> {
			assertEquals(0, ticketEntity.getId());
			assertEquals(ticketRequestDto.name(), ticketEntity.getName());
			assertEquals(ticketRequestDto.description(), ticketEntity.getDescription());
			assertEquals(LocalDate.now(), ticketEntity.getCreatedAt());
			assertEquals(null, ticketEntity.getUpdatedAt());
			assertEquals(projectEntity, ticketEntity.getProject());
			assertEquals(projectManagerEntity, ticketEntity.getCreator());
			assertEquals(TicketType.Bug, ticketEntity.getTicketType().getTicketType());
			assertEquals(TicketPriority.High, ticketEntity.getTicketPriority().getTicketPriority());
			assertEquals(TicketStatus.New, ticketEntity.getTicketStatus().getTicketStatus());
		});
	}
	
    /**
     * Tests the toDto method.
     * Expects correct mapping from entity to dto.
     */
	@Test
	public void toDto_MapFromEntityToDto_ReturnDto() {
		// Arrange
		TicketTypeEntity ticketTypeEntity = TestDataUtil.createTestTicketType(TicketType.Bug);
		TicketPriorityEntity ticketPriorityEntity = TestDataUtil.createTestTicketPriority(TicketPriority.High);
		TicketStatusEntity ticketStatusEntity = TestDataUtil.createTestTicketStatus(TicketStatus.New);
		TicketEntity ticketEntity = TestDataUtil.createTicket1(projectEntity, projectManagerEntity, ticketTypeEntity, ticketPriorityEntity, ticketStatusEntity);
		
		// Act
		TicketResponseDto ticketResponseDto = underTest.toDto(ticketEntity);

		// Assert
		assertAll(() -> {
			assertEquals(ticketEntity.getId(), ticketResponseDto.id());
			assertEquals(ticketEntity.getName(), ticketResponseDto.name());
			assertEquals(ticketEntity.getDescription(), ticketResponseDto.description());
			assertEquals(ticketEntity.getCreatedAt(), ticketResponseDto.createdAt());
			assertEquals(ticketEntity.getUpdatedAt(), ticketResponseDto.updatedAt());
			assertEquals(ticketEntity.getProject().getId(), ticketResponseDto.projectId());
			assertEquals(ticketEntity.getCreator().getId(), ticketResponseDto.creatorId());
			assertEquals(ticketEntity.getTicketType().getTicketType(), ticketResponseDto.ticketType());
			assertEquals(ticketEntity.getTicketPriority().getTicketPriority(), ticketResponseDto.ticketPriority());
			assertEquals(ticketEntity.getTicketStatus().getTicketStatus(), ticketResponseDto.ticketStatus());
		});
	}
	
    /**
     * Tests the update method.
     * Expects correct mapping and updating of entity.
     */
	@Test
	public void updatedEntity_UpdateEntityFields_ReturnEntity() {
		// Arrange
		TicketTypeEntity ticketTypeEntity = TestDataUtil.createTestTicketType(TicketType.Bug);
		TicketPriorityEntity ticketPriorityEntity = TestDataUtil.createTestTicketPriority(TicketPriority.High);
		TicketStatusEntity ticketStatusEntity = TestDataUtil.createTestTicketStatus(TicketStatus.New);
		TicketEntity ticketEntity = TestDataUtil.createTicket1(projectEntity, projectManagerEntity, ticketTypeEntity, ticketPriorityEntity, ticketStatusEntity);
		TicketType updatedTicketType = TicketType.ChangeRequest;
		TicketPriority updatedTicketPriority = TicketPriority.Medium;
		TicketStatus updatedTicketStatus = TicketStatus.Development;
		
		TicketRequestDto ticketRequestDto = new TicketRequestDto("Updated Name", "Updated Description", updatedTicketType.toString(), updatedTicketPriority.toString(), updatedTicketStatus.toString());
		
		// Mock calls
		when(ticketTypeService.getEntityByTicketType(updatedTicketType))
							  .thenReturn(Optional.of(TestDataUtil.createTestTicketType(updatedTicketType)));
		when(ticketPriorityService.getEntityByTicketPriority(updatedTicketPriority))
							  	  .thenReturn(Optional.of(TestDataUtil.createTestTicketPriority(updatedTicketPriority)));
		when(ticketStatusService.getEntityByTicketStatus(updatedTicketStatus))
		  						.thenReturn(Optional.of(TestDataUtil.createTestTicketStatus(updatedTicketStatus)));
		
		// Act
		TicketEntity updatedTicket = underTest.updateEntity(ticketEntity, ticketRequestDto);

		// Assert
		assertAll(() -> {
			assertEquals(ticketEntity.getId(), updatedTicket.getId());
			assertEquals(ticketEntity.getName(), updatedTicket.getName());
			assertEquals(ticketEntity.getDescription(), updatedTicket.getDescription());
			assertEquals(ticketEntity.getCreatedAt(), updatedTicket.getCreatedAt());
			assertEquals(LocalDate.now(), updatedTicket.getUpdatedAt());
			assertEquals(ticketEntity.getProject().getId(), updatedTicket.getProject().getId());
			assertEquals(ticketEntity.getCreator().getId(), updatedTicket.getCreator().getId());
			assertEquals(updatedTicketType, updatedTicket.getTicketType().getTicketType());
			assertEquals(updatedTicketPriority, updatedTicket.getTicketPriority().getTicketPriority());
			assertEquals(updatedTicketStatus, updatedTicket.getTicketStatus().getTicketStatus());
		});
	}
}
