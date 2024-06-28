package com.example.projecttrackingserver.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.projecttrackingserver.dto.CompanyRequestDto;
import com.example.projecttrackingserver.dto.CompanyResponseDto;
import com.example.projecttrackingserver.entities.CompanyEntity;

/**
 * Mapper interface for mapping company-related classes.
 */
@Mapper(componentModel = "spring")
public interface CompanyMapper {

    /**
     * Converts a CompanyRequestDto to a CompanyEntity.
     * 
     * @param dto the CompanyRequestDto to convert
     * @return the corresponding CompanyEntity
     */
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "projects", ignore = true)
	CompanyEntity toEntity(CompanyRequestDto dto);
	
    /**
     * Converts a CompanyEntity to a CompanyResponseDto.
     * 
     * @param entity the CompanyEntity to convert
     * @return the corresponding CompanyResponseDto
     */
	CompanyResponseDto toDto(CompanyEntity entity);
}