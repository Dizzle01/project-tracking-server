package com.example.projecttrackingserver.services;

import java.util.List;
import java.util.Optional;

import com.example.projecttrackingserver.dto.CompanyResponseDto;
import com.example.projecttrackingserver.entities.CompanyEntity;

/**
 * Service interface related to companies.
 */
public interface CompanyService {

    /**
     * Retrieves a list of CompanyResponseDto objects for all companies.
     *
     * @return a list of CompanyResponseDto objects
     */
	List<CompanyResponseDto> getAllCompanyDtos();
	
    /**
     * Retrieves a CompanyResponseDto object by its ID.
     *
     * @param companyId the ID of the company to retrieve
     * @return the CompanyResponseDto object if found
     */
	CompanyResponseDto getCompanyDtoById(long companyId);
	
	//CompanyResponseDto saveCompany(CompanyRequestDto companyDto);
	
    /**
     * Checks if a company entity exists by its name.
     *
     * @param companyName the name of the company
     * @return true if the company exists, false otherwise
     */
	boolean entityExists(String companyName);
	
    /**
     * Checks if a company entity exists by its ID.
     *
     * @param companyId the ID of the company
     * @return true if the company exists, false otherwise
     */
	boolean entityExists(long companyId);
	
    /**
     * Retrieves a company entity by its ID.
     *
     * @param companyId the ID of the company
     * @return an Optional containing the CompanyEntity if found, otherwise empty
     */
	Optional<CompanyEntity> getEntityById(long companyId);
}
