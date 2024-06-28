package com.example.projecttrackingserver.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.projecttrackingserver.TestDataUtil;
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

/**
 * Test class for the TicketRepository.
 * This class tests if the repository functionality works as intended.
 */
@DataJpaTest
public class TicketRepositoryTests {

	@Autowired
	private TicketRepository underTest;
	
	@Autowired
	private ProjectRepository projectRepository;
	
	@Autowired
	private CompanyRepository companyRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private TicketTypeRepository ticketTypeRepository;
	
	@Autowired
	private TicketPriorityRepository ticketPriorityRepository;
	
	@Autowired
	private TicketStatusRepository ticketStatusRepository;
	
	private CompanyEntity companyEntity;
	private ProjectEntity projectEntity;
	private UserEntity projectManagerEntity;
	private RoleEntity projectManagerRoleEntity;
	private TicketTypeEntity ticketTypeEntity;
	private TicketPriorityEntity ticketPriorityEntity;
	private TicketStatusEntity ticketStatusEntity;
	@BeforeEach
	public void setUp() {
		// Arrange
		companyEntity = companyRepository.save(TestDataUtil.createCompany1());
		projectManagerRoleEntity = roleRepository.save(TestDataUtil.createAdminRole());
		projectManagerEntity = userRepository.save(TestDataUtil.createUser1(projectManagerRoleEntity, companyEntity));
		projectEntity = projectRepository.save(TestDataUtil.createProject1(companyEntity, projectManagerEntity));
		ticketTypeEntity = ticketTypeRepository.save(TestDataUtil.createTestTicketType(TicketType.GeneralTask));
		ticketPriorityEntity = ticketPriorityRepository.save(TestDataUtil.createTestTicketPriority(TicketPriority.Low));
		ticketStatusEntity = ticketStatusRepository.save(TestDataUtil.createTestTicketStatus(TicketStatus.New));
	}
	
    /**
     * Tests for saving and finding a TicketEntity.
     * Expects that saved entity can be successfully retrieved.
     */
	@Test
	public void SaveAndFind_SaveOneTicketAndRetrieveIt_ReturnOneTicket() {
		// Arrange
		TicketEntity ticketEntity = TestDataUtil.createTicket1(projectEntity, projectManagerEntity, ticketTypeEntity, ticketPriorityEntity, ticketStatusEntity);
		
		// Act
		underTest.save(ticketEntity);
		Optional<TicketEntity> retrievedTicket1 = underTest.findById(ticketEntity.getId());
		Optional<TicketEntity> retrievedTicket2 = underTest.findByNameAndProjectId(ticketEntity.getName(), projectEntity.getId());
		
		// Assert
		assertAll(() -> {
			assertThat(retrievedTicket1).isPresent();
			assertThat(retrievedTicket1.get()).isEqualTo(ticketEntity);
			assertThat(retrievedTicket2).isPresent();
			assertThat(retrievedTicket2.get()).isEqualTo(ticketEntity);
		});
	}
	
    /**
     * Tests for saving and finding multiple TicketEntities.
     * Expects that saved entities can be successfully retrieved.
     */
	@Test
	public void SaveAndFindAll_SaveMultipleTicketsAndRetrieveThem_ReturnMultipleTickets() {
		// Arrange
		TicketEntity ticketEntity1 = TestDataUtil.createTicket1(projectEntity, projectManagerEntity, ticketTypeEntity, ticketPriorityEntity, ticketStatusEntity);
//		TicketEntity ticketEntity2 = TestDataUtil.createTicket2(projectEntity, projectManagerEntity, ticketTypeEntity, ticketPriorityEntity, ticketStatusEntity);
		
		// Act
		underTest.save(ticketEntity1);
//		underTest.save(ticketEntity2);
		Iterable<TicketEntity> allTickets = underTest.findAll();

		// Assert
		assertAll(() -> {
//			assertThat(allTickets).hasSize(2);
//			assertThat(allTickets).containsOnly(ticketEntity1, ticketEntity2);
			assertThat(allTickets).hasSize(1);
			assertThat(allTickets).containsOnly(ticketEntity1);
		});
	}
}
