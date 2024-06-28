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
import com.example.projecttrackingserver.entities.RoleEntity;
import com.example.projecttrackingserver.entities.UserEntity;

/**
 * Test class for the UserRepository.
 * This class tests if the repository functionality works as intended.
 */
@DataJpaTest
public class UserRepositoryTests {

	@Autowired
	private UserRepository underTest;	
	
	@Autowired
	private CompanyRepository companyRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	private RoleEntity adminRoleEntity;
	private RoleEntity developerRoleEntity;
	private CompanyEntity companyEntity;
	@BeforeEach
	public void setUp() {
		// Arrange
		adminRoleEntity = roleRepository.save(TestDataUtil.createAdminRole());
		developerRoleEntity = roleRepository.save(TestDataUtil.createDeveloperRole());
		companyEntity = companyRepository.save(TestDataUtil.createCompany1());
	}
	
    /**
     * Tests for saving and finding a UserEntity.
     * Expects that saved entity can be successfully retrieved.
     */
	@Test
	public void SaveAndFind_SaveOneUserAndRetrieveIt_ReturnOneUser() {
		// Arrange
		UserEntity userEntity = TestDataUtil.createUser1(developerRoleEntity, companyEntity);
		
		// Act
		underTest.save(userEntity);
		Optional<UserEntity> retrievedUser1 = underTest.findById(userEntity.getId());
		Optional<UserEntity> retrievedUser2 = underTest.findByIdAndCompanyId(userEntity.getId(), companyEntity.getId());
		Optional<UserEntity> retrievedUser3 = underTest.findByUsername(userEntity.getUsername());
		Optional<UserEntity> retrievedUser4 = underTest.findByApiKey(userEntity.getApiKey());
		
		// Assert
		assertAll(() -> {
			assertThat(retrievedUser1).isPresent();
			assertThat(retrievedUser1.get()).isEqualTo(userEntity);
			assertThat(retrievedUser2).isPresent();
			assertThat(retrievedUser2.get()).isEqualTo(userEntity);
			assertThat(retrievedUser3).isPresent();
			assertThat(retrievedUser3.get()).isEqualTo(userEntity);
			assertThat(retrievedUser4).isPresent();
			assertThat(retrievedUser4.get()).isEqualTo(userEntity);
		});
	}
	
    /**
     * Tests for saving and finding multiple UserEntities.
     * Expects that saved entities can be successfully retrieved.
     */
	@Test
	public void SaveAndFindAll_SaveMultipleUsersAndRetrieveThem_ReturnMultipleUsers() {
		// Arrange
		UserEntity testUser1 = TestDataUtil.createUser1(developerRoleEntity, companyEntity);
		UserEntity testUser2 = TestDataUtil.createUser2(adminRoleEntity, companyEntity);
		
		// Act
		underTest.save(testUser1);
		underTest.save(testUser2);
		Iterable<UserEntity> allUsers = underTest.findAllByCompanyId(companyEntity.getId());

		// Assert
		assertAll(() -> {
			assertThat(allUsers).hasSize(2);
			assertThat(allUsers).containsOnly(testUser1, testUser2);
		});
	}
}
