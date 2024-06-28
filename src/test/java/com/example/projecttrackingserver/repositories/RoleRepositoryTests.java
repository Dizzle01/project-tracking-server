package com.example.projecttrackingserver.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.projecttrackingserver.TestDataUtil;
import com.example.projecttrackingserver.entities.RoleEntity;
import com.example.projecttrackingserver.enums.Role;

/**
 * Test class for the RoleRepository.
 * This class tests if the repository functionality works as intended.
 */
@DataJpaTest
public class RoleRepositoryTests {

	@Autowired
	private RoleRepository underTest;
	
    /**
     * Tests for saving and finding a RoleEntity.
     * Expects that saved entity can be successfully retrieved.
     */
	@Test
	public void SaveAndFindByRole_SaveOneRoleAndRetrieveIt_ReturnOneRole() {
		// Arrange
		RoleEntity testRoleEntity = TestDataUtil.createAdminRole();
		
		// Act
		underTest.save(testRoleEntity);
		Optional<RoleEntity> retrievedRole = underTest.findByRole(Role.Admin);
		
		// Assert
		assertThat(retrievedRole).isPresent();
		assertThat(retrievedRole.get()).isEqualTo(testRoleEntity);
	}
}
