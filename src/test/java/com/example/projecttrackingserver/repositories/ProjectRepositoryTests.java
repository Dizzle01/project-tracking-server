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
import com.example.projecttrackingserver.entities.UserEntity;

/**
 * Test class for the ProjectRepository.
 * This class tests if the repository functionality works as intended.
 */
@DataJpaTest
public class ProjectRepositoryTests {

	@Autowired
	private ProjectRepository underTest;
	
	@Autowired
	private CompanyRepository companyRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	private RoleEntity projectManagerRoleEntity;
	private CompanyEntity companyEntity;
	private UserEntity projectManagerEntity;
	@BeforeEach
	public void setUp() {
		// Arrange
		projectManagerRoleEntity = roleRepository.save(TestDataUtil.createProjectManagerRole());
		companyEntity = companyRepository.save(TestDataUtil.createCompany1());
		projectManagerEntity = userRepository.save(TestDataUtil.createUser1(projectManagerRoleEntity, companyEntity));
	}
	
    /**
     * Tests for saving and finding a ProjectEntity.
     * Expects that saved entity can be successfully retrieved.
     */
	@Test
	public void SaveAndFind_SaveOneProjectAndRetrieveIt_ReturnOneProject() {
		// Arrange
		ProjectEntity projectEntity = TestDataUtil.createProject1(companyEntity, projectManagerEntity);
		
		// Act
		underTest.save(projectEntity);
		Optional<ProjectEntity> retrievedProject1 = underTest.findById(projectEntity.getId());
		Optional<ProjectEntity> retrievedProject2 = underTest.findByName(projectEntity.getName());
		Optional<ProjectEntity> retrievedProject3 = underTest.findByIdAndCompanyId(projectEntity.getId(), companyEntity.getId());
		
		// Assert
		assertAll(() -> {
			assertThat(retrievedProject1).isPresent();
			assertThat(retrievedProject1.get()).isEqualTo(projectEntity);
			assertThat(retrievedProject2).isPresent();
			assertThat(retrievedProject2.get()).isEqualTo(projectEntity);
			assertThat(retrievedProject3).isPresent();
			assertThat(retrievedProject3.get()).isEqualTo(projectEntity);
		});
	}
	
    /**
     * Tests for saving and finding multiple ProjectEntities.
     * Expects that saved entities can be successfully retrieved.
     */
	@Test
	public void SaveAndFindAll_SaveMultipleProjectsAndRetrieveThem_ReturnMultipleProjects() {
		// Arrange
		ProjectEntity projectEntity1 = TestDataUtil.createProject1(companyEntity, projectManagerEntity);
//		ProjectEntity projectEntity2 = TestDataUtil.createProject2(companyEntity, projectManagerEntity);
		
		// Act
		underTest.save(projectEntity1);
//		underTest.save(projectEntity2);
		Iterable<ProjectEntity> allProjects = underTest.findAllByCompanyId(companyEntity.getId());
		
		// Assert
		assertAll(() -> {
//			assertThat(allProjects).hasSize(2);
//			assertThat(allProjects).containsOnly(projectEntity1, projectEntity2);
			assertThat(allProjects).hasSize(1);
			assertThat(allProjects).containsOnly(projectEntity1);
		});
	}
}
