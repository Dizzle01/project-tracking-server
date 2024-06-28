package com.example.projecttrackingserver.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.projecttrackingserver.TestDataUtil;
import com.example.projecttrackingserver.entities.TicketTypeEntity;
import com.example.projecttrackingserver.enums.TicketType;

/**
 * Test class for the TicketTypeRepository.
 * This class tests if the repository functionality works as intended.
 */
@DataJpaTest
public class TicketTypeRepositoryTests {

	@Autowired
	private TicketTypeRepository underTest;
	
    /**
     * Tests for saving and finding a TicketTypeEntity.
     * Expects that saved entity can be successfully retrieved.
     */
	@Test
	public void SaveAndFindByTicketType_SaveOneTicketTypeAndRetrieveIt_ReturnOneTicketType() {
		// Arrange
		TicketType ticketType = TicketType.GeneralTask;
		TicketTypeEntity testTypeEntity = TestDataUtil.createTestTicketType(ticketType);
		
		// Act
		underTest.save(testTypeEntity);
		Optional<TicketTypeEntity> retrievedTicketType = underTest.findByTicketType(ticketType);
		
		// Assert
		assertThat(retrievedTicketType).isPresent();
		assertThat(retrievedTicketType.get()).isEqualTo(testTypeEntity);
	}
}
