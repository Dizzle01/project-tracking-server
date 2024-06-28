package com.example.projecttrackingserver.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.projecttrackingserver.dto.CompanyResponseDto;
import com.example.projecttrackingserver.services.CompanyService;

import lombok.RequiredArgsConstructor;

/**
 * Controller class for handling company-related HTTP requests.
 */
@RestController
@RequestMapping("/api/v1/companies")
@RequiredArgsConstructor
public class CompanyController {

	private final CompanyService companyService;
	
    /**
     * Endpoint to retrieve all companies.
     *
     * @return ResponseEntity containing a list of CompanyResponseDto and HTTP status code OK (200)
     */
	@GetMapping
	public ResponseEntity<List<CompanyResponseDto>> getAllCompanies() {
		return new ResponseEntity<List<CompanyResponseDto>>(
				companyService.getAllCompanyDtos(),
				HttpStatus.OK);
	}
	
    /**
     * Endpoint to retrieve a specific company by its ID.
     *
     * @param companyId ID of the company to retrieve
     * @return ResponseEntity containing the CompanyResponseDto and HTTP status code OK (200)
     */
	@GetMapping("/{companyId}")
	public ResponseEntity<CompanyResponseDto> getOneCompany(
			@PathVariable long companyId
	) {
		return new ResponseEntity<CompanyResponseDto>(
				companyService.getCompanyDtoById(companyId),
				HttpStatus.OK);
	}
}