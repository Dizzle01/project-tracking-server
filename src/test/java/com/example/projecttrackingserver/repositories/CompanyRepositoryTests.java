package com.example.projecttrackingserver.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.projecttrackingserver.TestDataUtil;
import com.example.projecttrackingserver.entities.CompanyEntity;

/**
 * Test class for the CompanyRepository.
 * This class tests if the repository functionality works as intended.
 */
@DataJpaTest
public class CompanyRepositoryTests {

	@Autowired
	private CompanyRepository underTest;
	
    /**
     * Tests for saving and finding a CompanyEntity.
     * Expects that saved entity can be successfully retrieved.
     */
	@Test
	public void SaveAndFindById_SaveOneCompanyAndRetrieveIt_ReturnOneCompany() {
		// Arrange
		CompanyEntity companyEntity = TestDataUtil.createCompany1();
		
		// Act
		underTest.save(companyEntity);
		Optional<CompanyEntity> retrievedCompany = underTest.findById(companyEntity.getId());
		
		// Assert
		assertAll(() -> {
			assertThat(retrievedCompany).isPresent();
			assertThat(retrievedCompany.get()).isEqualTo(companyEntity);
		});
	}
	
    /**
     * Tests for saving and finding multiple CompanyEntities.
     * Expects that saved entities can be successfully retrieved.
     */
	@Test
	public void SaveAndFindAll_SaveMultipleCompaniesAndRetrieveThem_ReturnMultipleCompanies() {
		// Arrange
		CompanyEntity companyEntity1 = TestDataUtil.createCompany1();
		CompanyEntity companyEntity2 = TestDataUtil.createCompany2();
		
		// Act
		underTest.save(companyEntity1);
		underTest.save(companyEntity2);	
		Iterable<CompanyEntity> allCompanies = underTest.findAll();
		
		// Assert
		assertAll(() -> {
			assertThat(allCompanies).hasSize(2);
			assertThat(allCompanies).containsOnly(companyEntity1, companyEntity2);
		});
	}
}
