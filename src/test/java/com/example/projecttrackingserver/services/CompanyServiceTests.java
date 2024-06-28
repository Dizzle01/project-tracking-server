package com.example.projecttrackingserver.services;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.projecttrackingserver.TestDataUtil;
import com.example.projecttrackingserver.dto.CompanyResponseDto;
import com.example.projecttrackingserver.entities.CompanyEntity;
import com.example.projecttrackingserver.exceptions.EntityNotFoundException;
import com.example.projecttrackingserver.mappers.CompanyMapper;
import com.example.projecttrackingserver.mappers.CompanyMapperImpl;
import com.example.projecttrackingserver.repositories.CompanyRepository;

/**
 * Test class for the CompanyServiceImpl.
 * This class tests if the CompanyService functionality works as intended.
 */
@ExtendWith(MockitoExtension.class)
public class CompanyServiceTests {

	@InjectMocks
	private CompanyServiceImpl underTest;
	
	@Mock
	private CompanyRepository companyRepository;
	
	@Spy
	private CompanyMapper companyMapper = new CompanyMapperImpl();
	
	//private CompanyEntity companyMock;
	private CompanyEntity companyEntity1;
	private CompanyEntity companyEntity2;
	private long validCompanyId;
	private long invalidCompanyId;
	@BeforeEach
	public void setUp() {
		// Arrange
		//companyMock = Mockito.mock(CompanyEntity.class);
		companyEntity1 = TestDataUtil.createCompany1();
		companyEntity2 = TestDataUtil.createCompany2();
		validCompanyId = companyEntity1.getId();
		invalidCompanyId = 100;
	}
	
    /**
     * Tests retrieving multiple companies.
     * Expects that multiple companies are successfully mapped and retrieved.
     */
	@Test
	public void GetAllCompanyDtos_RetrieveMultipleCompanies_ReturnMultipleCompanyResponses() {
		// Arrange
		List<CompanyEntity> companies = Arrays.asList(companyEntity1, companyEntity2);
		
		// Mock
		when(companyRepository.findAll())
							  .thenReturn(companies);
		
		// Act
		List<CompanyResponseDto> companyResponseDtos = underTest.getAllCompanyDtos();
		
		// Assert
		assertEquals(companies.size(), companyResponseDtos.size());
		for(CompanyEntity company : companies) {
			verify(companyMapper, times(1)).toDto(company);
		}
		verify(companyRepository, times(1)).findAll();
	}
	
    /**
     * Tests retrieving one company.
     * Expects that one company is successfully mapped and retrieved.
     */
	@Test
	public void GetCompanyDtoById_RetrieveOneCompany_ReturnOneCompanyResponse() {
		// Mock
		when(companyRepository.findById(validCompanyId))
							  .thenReturn(Optional.of(companyEntity1));
		when(companyRepository.findById(invalidCompanyId))
		  					  .thenReturn(Optional.empty());
		
		// Act
		CompanyResponseDto companyResponseDto = underTest.getCompanyDtoById(validCompanyId);
		
		// Assert
		assertAll(() -> {
			assertEquals(companyEntity1.getId(), companyResponseDto.id());
			assertEquals(companyEntity1.getName(), companyResponseDto.name());
			assertEquals(companyEntity1.getDescription(), companyResponseDto.description());
			assertThrows(EntityNotFoundException.class, () -> underTest.getCompanyDtoById(invalidCompanyId));
		});
		verify(companyMapper, times(1)).toDto(companyEntity1);
		verify(companyRepository, times(1)).findById(validCompanyId);
	}
	
//	@Test
//	public void CompanyService_SaveOneCompany_ReturnOneCompanyResponse() {
//		// Arrange
//		String notExistingUsername = "NotExistingCompanyName";
//		String alreadyExistingUsername = "ExistingCompanyName";
//		CompanyRequestDto companyRequestDto1 = new CompanyRequestDto(notExistingUsername, "TestDescription");
//		CompanyRequestDto companyRequestDto2 = new CompanyRequestDto(alreadyExistingUsername, "TestDescription");
//		
//		// Mock
//		when(companyRepository.save(Mockito.any(CompanyEntity.class)))
//							  .thenReturn(companyEntity1);
//		when(companyRepository.findByName(notExistingUsername))
//							  .thenReturn(Optional.empty());
//		when(companyRepository.findByName(alreadyExistingUsername))
//							  .thenReturn(Optional.of(companyMock));
//		
//		// Act
//		CompanyResponseDto companyResponseDto = underTest.saveCompany(companyRequestDto1);
//		
//		// Assert
//		assertAll(() -> {
//			assertEquals(companyEntity1.getId(), companyResponseDto.id());
//			assertEquals(companyEntity1.getName(), companyResponseDto.name());
//			assertEquals(companyEntity1.getDescription(), companyResponseDto.description());
//			assertThrows(EntityAlreadyExistsException.class, () -> underTest.saveCompany(companyRequestDto2));
//		});
//		verify(companyMapper, times(1)).toEntity(companyRequestDto1);
//		verify(companyRepository, times(1)).save(Mockito.any(CompanyEntity.class));
//		verify(companyMapper, times(1)).toDto(companyEntity1);
//	}
}
