package com.example.projecttrackingserver.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.projecttrackingserver.TestDataUtil;
import com.example.projecttrackingserver.entities.TicketPriorityEntity;
import com.example.projecttrackingserver.enums.TicketPriority;

/**
 * Test class for the TicketPriorityRepository.
 * This class tests if the repository functionality works as intended.
 */
@DataJpaTest
public class TicketPriorityRepositoryTests {

	@Autowired
	private TicketPriorityRepository underTest;
	
    /**
     * Tests for saving and finding a TicketPriorityEntity.
     * Expects that saved entity can be successfully retrieved.
     */
	@Test
	public void SaveAndFindByTicketPriority_SaveOneTicketPriorityAndRetrieveIt_ReturnOneTicketPriority() {
		// Arrange
		TicketPriority ticketPriority = TicketPriority.Low;
		TicketPriorityEntity testPriorityEntity = TestDataUtil.createTestTicketPriority(ticketPriority);
		
		// Act
		underTest.save(testPriorityEntity);
		Optional<TicketPriorityEntity> retrievedTicketPriority = underTest.findByTicketPriority(ticketPriority);
		
		// Assert
		assertThat(retrievedTicketPriority).isPresent();
		assertThat(retrievedTicketPriority.get()).isEqualTo(testPriorityEntity);
	}
}
