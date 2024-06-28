package com.example.projecttrackingserver.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.example.projecttrackingserver.dto.ProjectRequestDto;
import com.example.projecttrackingserver.dto.ProjectResponseDto;
import com.example.projecttrackingserver.entities.CompanyEntity;
import com.example.projecttrackingserver.entities.ProjectEntity;
import com.example.projecttrackingserver.entities.UserEntity;

/**
 * Mapper interface for mapping project-related classes.
 */
@Mapper(componentModel = "spring")
public interface ProjectMapper {
	
    /**
     * Converts a ProjectRequestDto along with CompanyEntity and UserEntity to a ProjectEntity.
     * 
     * @param dto the ProjectRequestDto to convert
     * @param companyEntity the CompanyEntity associated with the project
     * @param projectManagerEntity the UserEntity representing the project manager
     * @return the corresponding ProjectEntity
     */
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "members", ignore = true)
	@Mapping(target = "tickets", ignore = true)
	@Mapping(source = "dto.projectName", target = "name")
	@Mapping(source = "dto.description", target = "description")
	@Mapping(source = "companyEntity", target = "company")
	@Mapping(source = "projectManagerEntity", target = "projectManager")
	@Mapping(source = "dto.startDate", target = "startDate", dateFormat = "dd-MM-yyyy")
	@Mapping(source = "dto.endDate", target = "endDate", dateFormat = "dd-MM-yyyy")
	ProjectEntity toEntity(ProjectRequestDto dto, CompanyEntity companyEntity, UserEntity projectManagerEntity);
	
    /**
     * Converts a ProjectEntity to a ProjectResponseDto.
     * 
     * @param entity the ProjectEntity to convert
     * @return the corresponding ProjectResponseDto
     */
	@Mapping(source = "id", target = "id")
	@Mapping(source = "name", target = "projectName")
	@Mapping(source = "projectManager.id", target = "projectManagerId")
	ProjectResponseDto toDto(ProjectEntity entity);
	
    /**
     * Updates an existing ProjectEntity with values from ProjectRequestDto and UserEntity.
     * 
     * @param projectEntity the existing ProjectEntity to update
     * @param dto the ProjectRequestDto containing updated values
     * @param projectManager the UserEntity representing the updated project manager
     * @return the updated ProjectEntity
     */
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "company", ignore = true)
	@Mapping(target = "members", ignore = true)
	@Mapping(target = "tickets", ignore = true)
	@Mapping(source = "dto.projectName", target = "name", defaultExpression = "java(projectEntity.getName())")
	@Mapping(source = "dto.description", target = "description", defaultExpression = "java(projectEntity.getDescription())")
	@Mapping(source = "dto.startDate", target = "startDate", defaultExpression = "java(projectEntity.getStartDate())", dateFormat = "dd-MM-yyyy")
	@Mapping(source = "dto.endDate", target = "endDate", defaultExpression = "java(projectEntity.getEndDate())", dateFormat = "dd-MM-yyyy")
	@Mapping(source = "projectManager", target = "projectManager", defaultExpression = "java(projectEntity.getProjectManager())")
	ProjectEntity updateEntity(@MappingTarget ProjectEntity projectEntity, ProjectRequestDto dto, UserEntity projectManager);
}
