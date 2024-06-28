package com.example.projecttrackingserver.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.projecttrackingserver.TestDataUtil;
import com.example.projecttrackingserver.entities.TicketStatusEntity;
import com.example.projecttrackingserver.enums.TicketStatus;

/**
 * Test class for the TicketStatusRepository.
 * This class tests if the repository functionality works as intended.
 */
@DataJpaTest
public class TicketStatusRepositoryTests {

	@Autowired
	private TicketStatusRepository underTest;
	
    /**
     * Tests for saving and finding a TicketStatusEntity.
     * Expects that saved entity can be successfully retrieved.
     */
	@Test
	public void SaveAndFindByTicketStatus_SaveOneTicketStatusAndRetrieveIt_ReturnOneTicketStatus() {
		// Arrange
		TicketStatus ticketStatus = TicketStatus.New;
		TicketStatusEntity testStatusEntity = TestDataUtil.createTestTicketStatus(ticketStatus);
		
		// Act
		underTest.save(testStatusEntity);
		Optional<TicketStatusEntity> retrievedTicketStatus = underTest.findByTicketStatus(ticketStatus);
		
		// Assert
		assertThat(retrievedTicketStatus).isPresent();
		assertThat(retrievedTicketStatus.get()).isEqualTo(testStatusEntity);
	}
}
