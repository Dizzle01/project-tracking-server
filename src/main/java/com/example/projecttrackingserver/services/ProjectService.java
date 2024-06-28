package com.example.projecttrackingserver.services;

import java.util.List;
import java.util.Optional;

import com.example.projecttrackingserver.dto.ProjectRequestDto;
import com.example.projecttrackingserver.dto.ProjectResponseDto;
import com.example.projecttrackingserver.dto.UserResponseDto;
import com.example.projecttrackingserver.entities.ProjectEntity;

/**
 * Service interface related to projects.
 */
public interface ProjectService {

    /**
     * Retrieves a list of ProjectResponseDto objects for all projects in a company.
     *
     * @param companyId the ID of the company
     * @return a list of ProjectResponseDto objects
     */
	List<ProjectResponseDto> getAllProjectDtosInCompany(long companyId);
	
    /**
     * Retrieves a ProjectResponseDto object for a specific project in a company.
     *
     * @param companyId the ID of the company
     * @param projectId the ID of the project
     * @return the ProjectResponseDto object if found
     */
	ProjectResponseDto getProjectDtoInCompany(long companyId, long projectId);
	
    /**
     * Retrieves a list of UserResponseDto objects for all users in a project.
     *
     * @param companyId the ID of the company
     * @param projectId the ID of the project
     * @return a list of UserResponseDto objects
     */
	List<UserResponseDto> getAllUserDtosInProject(long companyId, long projectId);
	
    /**
     * Creates a new project in a company.
     *
     * @param projectRequestDto the project details
     * @param companyId the ID of the company
     * @return the created ProjectResponseDto object
     */
	ProjectResponseDto createProject(ProjectRequestDto projectRequestDto, long companyId);
	
    /**
     * Deletes a project from a company.
     *
     * @param companyId the ID of the company
     * @param projectId the ID of the project
     */
	void deleteProject(long companyId, long projectId);
	
    /**
     * Updates an existing project in a company.
     *
     * @param projectRequestDto the updated project details
     * @param companyId the ID of the company
     * @param projectId the ID of the project
     * @return the updated ProjectResponseDto object
     */
	ProjectResponseDto updateProject(ProjectRequestDto projectRequestDto, long companyId, long projectId);
	
    /**
     * Adds or removes a user from a project.
     *
     * @param companyId the ID of the company
     * @param projectId the ID of the project
     * @param userId the ID of the user
     * @param addingUser true if adding the user, false if removing
     */
	void alterProjectMembersInProject(long companyId, long projectId, long userId, boolean addingUser);

    /**
     * Checks if a project entity exists by its name.
     *
     * @param projectName the name of the project
     * @return true if the project exists, false otherwise
     */
	boolean entityExists(String projectName);

    /**
     * Checks if a project entity exists by its ID and company ID.
     *
     * @param projectId the ID of the project
     * @param companyId the ID of the company
     * @return true if the project exists in the company, false otherwise
     */
	boolean entityExists(long projectId, long companyId);
	
    /**
     * Retrieves a project entity by its ID and company ID.
     *
     * @param projectId the ID of the project
     * @param companyId the ID of the company
     * @return an Optional containing the ProjectEntity if found, otherwise empty
     */
	Optional<ProjectEntity> getEntityByIdAndCompanyId(long projectId, long companyId);
}
