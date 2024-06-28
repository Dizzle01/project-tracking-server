package com.example.projecttrackingserver.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Service;

import com.example.projecttrackingserver.dto.CompanyResponseDto;
import com.example.projecttrackingserver.entities.CompanyEntity;
import com.example.projecttrackingserver.exceptions.EntityNotFoundException;
import com.example.projecttrackingserver.mappers.CompanyMapper;
import com.example.projecttrackingserver.repositories.CompanyRepository;

import lombok.RequiredArgsConstructor;

/**
 * Implementation of {@link CompanyService} interface 
 */
@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

	private final CompanyRepository companyRepository;
	private final CompanyMapper companyMapper;
	
    /**
     * {@inheritDoc}
     */
	public List<CompanyResponseDto> getAllCompanyDtos() {
		return StreamSupport.stream(companyRepository.findAll().spliterator(), false)
							.map(companyEntity -> companyMapper.toDto(companyEntity))
					 		.collect(Collectors.toList());
	}
	
    /**
     * {@inheritDoc}
     */
	public CompanyResponseDto getCompanyDtoById(long companyId) {
		// company does not exist -> deny
		Optional<CompanyEntity> companyEntity = companyRepository.findById(companyId);
		if(companyEntity.isEmpty()) {
			throw new EntityNotFoundException("companyId", companyId);
		}

		return companyMapper.toDto(companyEntity.get());
	}
	
//	public CompanyResponseDto saveCompany(CompanyRequestDto companyDto) {
//		// company with name already exists -> deny
//		if(entityExists(companyDto.name())) {
//			throw new EntityAlreadyExistsException("name", companyDto.name());
//		}
//		
//		CompanyEntity companyEntity = companyMapper.toEntity(companyDto);
//		
//		companyEntity = companyRepository.save(companyEntity);
//		
//		return companyMapper.toDto(companyEntity);
//	}
	
    /**
     * {@inheritDoc}
     */
	public boolean entityExists(String companyName) {
		return companyRepository.findByName(companyName).isPresent();
	}
	
    /**
     * {@inheritDoc}
     */
	public boolean entityExists(long companyId) {
		return companyRepository.findById(companyId).isPresent();
	}
	
    /**
     * {@inheritDoc}
     */
	public Optional<CompanyEntity> getEntityById(long companyId) {
		return companyRepository.findById(companyId);
	}
}
