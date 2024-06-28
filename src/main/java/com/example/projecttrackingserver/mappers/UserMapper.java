package com.example.projecttrackingserver.mappers;

import java.time.LocalDate;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.example.projecttrackingserver.auth.ApiKeyGenerator;
import com.example.projecttrackingserver.dto.UserRequestDto;
import com.example.projecttrackingserver.dto.UserResponseDto;
import com.example.projecttrackingserver.entities.CompanyEntity;
import com.example.projecttrackingserver.entities.RoleEntity;
import com.example.projecttrackingserver.entities.UserEntity;

/**
 * Mapper interface for mapping user-related classes.
 */
@Mapper(componentModel = "spring", imports = { ApiKeyGenerator.class, LocalDate.class })
public interface UserMapper {
	
	/**
	 * Maps UserRequestDto along with associated CompanyEntity and RoleEntity to UserEntity.
	 * 
	 * @param dto the UserRequestDto to map
	 * @param companyEntity the associated CompanyEntity
	 * @param roleEntity the RoleEntity representing the user's role
	 * @return the mapped UserEntity
	 */
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "projects", ignore = true)
	@Mapping(target = "apiKey", expression = "java(ApiKeyGenerator.generateApiKey())")
	@Mapping(target = "createdAt", expression = "java(LocalDate.now())")
	@Mapping(source = "companyEntity", target = "company")
	@Mapping(source = "roleEntity", target = "role")
    UserEntity toEntity(UserRequestDto dto, CompanyEntity companyEntity, RoleEntity roleEntity);
	
	/**
	 * Maps UserEntity to UserResponseDto.
	 * 
	 * @param entity the UserEntity to map
	 * @return the mapped UserResponseDto
	 */
	@Mapping(source = "company.name", target = "company")
	@Mapping(source = "role.role", target = "role")
    UserResponseDto toDto(UserEntity entity);
	
	/**
	 * Updates an existing UserEntity with values from UserRequestDto.
	 * 
	 * @param userEntity the existing UserEntity to update
	 * @param dto the UserRequestDto containing updated values
	 * @return the updated UserEntity
	 */
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "apiKey", ignore = true)
	@Mapping(target = "company", ignore = true)
	@Mapping(target = "role", ignore = true)
	@Mapping(target = "projects", ignore = true)
	@Mapping(source = "dto.username", target = "username", defaultExpression = "java(userEntity.getUsername())")
	UserEntity updateEntity(@MappingTarget UserEntity userEntity, UserRequestDto dto);
}