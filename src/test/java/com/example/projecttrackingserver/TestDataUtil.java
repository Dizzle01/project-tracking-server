package com.example.projecttrackingserver;

import java.time.LocalDate;

import com.example.projecttrackingserver.entities.CompanyEntity;
import com.example.projecttrackingserver.entities.ProjectEntity;
import com.example.projecttrackingserver.entities.RoleEntity;
import com.example.projecttrackingserver.entities.TicketEntity;
import com.example.projecttrackingserver.entities.TicketPriorityEntity;
import com.example.projecttrackingserver.entities.TicketStatusEntity;
import com.example.projecttrackingserver.entities.TicketTypeEntity;
import com.example.projecttrackingserver.entities.UserEntity;
import com.example.projecttrackingserver.enums.Role;
import com.example.projecttrackingserver.enums.TicketPriority;
import com.example.projecttrackingserver.enums.TicketStatus;
import com.example.projecttrackingserver.enums.TicketType;

import lombok.RequiredArgsConstructor;

/**
 * Utility class for creating test data entities.
 * This class contains static methods for generating predefined test data.
 */
@RequiredArgsConstructor
public final class TestDataUtil {
	
    /**
     * Creates a test TicketStatusEntity.
     *
     * @param ticketStatus the ticket status to be used in the entity
     * @return a TicketStatusEntity with predefined test data
     */
	public static TicketStatusEntity createTestTicketStatus(TicketStatus ticketStatus) {
		return TicketStatusEntity.builder()
								 .id(1L)
								 .ticketStatus(ticketStatus)
								 .build();
	}
	
    /**
     * Creates a test TicketPriorityEntity.
     *
     * @param ticketPriority the ticket priority to be used in the entity
     * @return a TicketPriorityEntity with predefined test data
     */
	public static TicketPriorityEntity createTestTicketPriority(TicketPriority ticketPriority) {
		return TicketPriorityEntity.builder()
								   .id(1L)
								   .ticketPriority(ticketPriority)
								   .build();
	}
	
    /**
     * Creates a test TicketTypeEntity.
     *
     * @param ticketType the ticket type to be used in the entity
     * @return a TicketTypeEntity with predefined test data
     */
	public static TicketTypeEntity createTestTicketType(TicketType ticketType) {
		return TicketTypeEntity.builder()
							   .id(1L)
							   .ticketType(ticketType)
							   .build();
	}
	
    /**
     * Creates an admin role entity for testing.
     *
     * @return a RoleEntity with admin role and predefined test data
     */
	public static RoleEntity createAdminRole() {
		return RoleEntity.builder()
						 .id(1L)
						 .role(Role.Admin)
						 .build();
	}
	
    /**
     * Creates a project manager role entity for testing.
     *
     * @return a RoleEntity with project manager role and predefined test data
     */
	public static RoleEntity createProjectManagerRole() {
		return RoleEntity.builder()
						 .id(2L)
						 .role(Role.ProjectManager)
						 .build();
	}
	
    /**
     * Creates a developer role entity for testing.
     *
     * @return a RoleEntity with developer role and predefined test data
     */
	public static RoleEntity createDeveloperRole() {
		return RoleEntity.builder()
						 .id(3L)
						 .role(Role.Developer)
						 .build();
	}
	
    /**
     * Creates a test company entity with ID 1.
     *
     * @return a CompanyEntity with predefined test data
     */
	public static CompanyEntity createCompany1() {
		return CompanyEntity.builder()
							.id(1L)
							.name("TestCompany1")
							.description("TestDescription1")
							.projects(null)
							.build();
	}
	
    /**
     * Creates a test company entity with ID 2.
     *
     * @return a CompanyEntity with predefined test data
     */
	public static CompanyEntity createCompany2() {
		return CompanyEntity.builder()
							.id(2L)
							.name("TestCompany2")
							.description("TestDescription2")
							.projects(null)
							.build();
	}
	
    /**
     * Creates a test user entity with ID 1.
     *
     * @param role the role to be assigned to the user
     * @param company the company to be assigned to the user
     * @return a UserEntity with predefined test data
     */
	public static UserEntity createUser1(RoleEntity role, CompanyEntity company) {
		return UserEntity.builder()
						 .id(1L)
						 .username("TestUser1")
						 .apiKey("4175f6fbfeaa3cf790a2e6f2d149208907307ddbedc2f283865fc2c8cf7f269e")
						 .createdAt(LocalDate.of(2024, 1, 1))
						 .company(company)
						 .role(role)
						 .projects(null)
						 .build();
	}
	
    /**
     * Creates a test user entity with ID 2.
     *
     * @param role the role to be assigned to the user
     * @param company the company to be assigned to the user
     * @return a UserEntity with predefined test data
     */
	public static UserEntity createUser2(RoleEntity role, CompanyEntity company) {
		return UserEntity.builder()
						 .id(2L)
						 .username("TestUser2")
						 .apiKey("91b19bddb1defef9d6abe69bb521158470fd618936703d1be739594206b298e2")
						 .createdAt(LocalDate.of(2024, 2, 2))
						 .company(company)
						 .role(role)
						 .projects(null)
						 .build();
	}
	
    /**
     * Creates a test project entity with ID 1.
     *
     * @param company the company to be assigned to the project
     * @param projectManager the project manager to be assigned to the project
     * @return a ProjectEntity with predefined test data
     */
	public static ProjectEntity createProject1(CompanyEntity company, UserEntity projectManager) {
		return ProjectEntity.builder()
							.id(1L)
							.name("TestProject1")
							.description("TestDescritpion1")
							.startDate(LocalDate.of(2020, 1, 1))
							.endDate(LocalDate.of(2021, 1, 1))
							.projectManager(projectManager)
							.members(null)
							.company(company)
							.tickets(null)
							.build();
	}
	
    /**
     * Creates a test project entity with ID 2.
     *
     * @param company the company to be assigned to the project
     * @param projectManager the project manager to be assigned to the project
     * @return a ProjectEntity with predefined test data
     */
	public static ProjectEntity createProject2(CompanyEntity company, UserEntity projectManager) {
		return ProjectEntity.builder()
							.id(2L)
							.name("TestProject2")
							.description("TestDescritpion2")
							.startDate(LocalDate.of(2020, 2, 2))
							.endDate(LocalDate.of(2021, 2, 2))
							.projectManager(projectManager)
							.members(null)
							.company(company)
							.tickets(null)
							.build();
	}
	
    /**
     * Creates a test ticket entity with ID 1.
     *
     * @param project the project to be assigned to the ticket
     * @param creator the user who created the ticket
     * @param ticketType the type of the ticket
     * @param ticketPriority the priority of the ticket
     * @param ticketStatus the status of the ticket
     * @return a TicketEntity with predefined test data
     */
	public static TicketEntity createTicket1(ProjectEntity project, UserEntity creator, TicketTypeEntity ticketType, TicketPriorityEntity ticketPriority, TicketStatusEntity ticketStatus) {
		return TicketEntity.builder()
						   .id(1L)
						   .name("TestTicket1")
						   .description("TestDescritpion1")
						   .createdAt(LocalDate.of(2020, 3, 3))
						   .updatedAt(null)
						   .project(project)
						   .creator(creator)
						   .ticketType(ticketType)
						   .ticketPriority(ticketPriority)
						   .ticketStatus(ticketStatus)
						   .build();
	}
	
    /**
     * Creates a test ticket entity with ID 2.
     *
     * @param project the project to be assigned to the ticket
     * @param creator the user who created the ticket
     * @param ticketType the type of the ticket
     * @param ticketPriority the priority of the ticket
     * @param ticketStatus the status of the ticket
     * @return a TicketEntity with predefined test data
     */
	public static TicketEntity createTicket2(ProjectEntity project, UserEntity creator, TicketTypeEntity ticketType, TicketPriorityEntity ticketPriority, TicketStatusEntity ticketStatus) {
		return TicketEntity.builder()
				   .id(2L)
				   .name("TestTicket2")
				   .description("TestDescritpion2")
				   .createdAt(LocalDate.of(2020, 4, 4))
				   .updatedAt(null)
				   .project(project)
				   .creator(creator)
				   .ticketType(ticketType)
				   .ticketPriority(ticketPriority)
				   .ticketStatus(ticketStatus)
				   .build();
	}
}
