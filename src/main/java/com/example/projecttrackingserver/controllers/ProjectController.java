package com.example.projecttrackingserver.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.projecttrackingserver.dto.ProjectRequestDto;
import com.example.projecttrackingserver.dto.ProjectResponseDto;
import com.example.projecttrackingserver.dto.UserResponseDto;
import com.example.projecttrackingserver.services.ProjectService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Controller class for handling project-related HTTP requests.
 */
@RestController
@RequestMapping(path = "/api/v1/companies/{companyId}/projects")
@RequiredArgsConstructor
public class ProjectController {

	private final ProjectService projectService;
	
    /**
     * Endpoint to retrieve all projects in a company.
     *
     * @param companyId ID of the company to retrieve projects for
     * @return ResponseEntity containing a list of ProjectResponseDto and HTTP status code OK (200)
     */
	@GetMapping
	public ResponseEntity<List<ProjectResponseDto>> getAllProjectsInCompany(
			@PathVariable long companyId
	) {
		return new ResponseEntity<List<ProjectResponseDto>>(
				projectService.getAllProjectDtosInCompany(companyId),
				HttpStatus.OK);
	}
	
    /**
     * Endpoint to retrieve a specific project in a company by its ID.
     *
     * @param companyId ID of the company that owns the project
     * @param projectId ID of the project to retrieve
     * @return ResponseEntity containing the ProjectResponseDto and HTTP status code OK (200)
     */
	@GetMapping(path = "/{projectId}")
	public ResponseEntity<ProjectResponseDto> getOneProjectInCompany(
			@PathVariable long companyId,
			@PathVariable long projectId
	) {
		return new ResponseEntity<ProjectResponseDto>(
				projectService.getProjectDtoInCompany(companyId, projectId),
				HttpStatus.OK);
	}
	
    /**
     * Endpoint to retrieve all users associated with a specific project in a company.
     *
     * @param companyId ID of the company that owns the project
     * @param projectId ID of the project to retrieve users for
     * @return ResponseEntity containing a list of UserResponseDto and HTTP status code OK (200)
     */
	@GetMapping(path = "/{projectId}/users")
	public ResponseEntity<List<UserResponseDto>> getAllUsersInProject(
			@PathVariable long companyId,
			@PathVariable long projectId
	) {
		return new ResponseEntity<List<UserResponseDto>>(
				projectService.getAllUserDtosInProject(companyId, projectId),
				HttpStatus.OK);
	}
	
    /**
     * Endpoint to create a new project in a company.
     *
     * @param projectRequestDto The details of the project to be created
     * @param companyId ID of the company where the project will be created
     * @return ResponseEntity containing the ProjectResponseDto of the created project and HTTP status code CREATED (201)
     */
	@PostMapping
	public ResponseEntity<ProjectResponseDto> createOneProject(
			@Valid @RequestBody ProjectRequestDto projectRequestDto,
			@PathVariable long companyId
	) {
		return new ResponseEntity<ProjectResponseDto>(
				projectService.createProject(projectRequestDto, companyId),
				HttpStatus.CREATED);
	}
	
    /**
     * Endpoint to update an existing project in a company by its ID.
     *
     * @param projectRequestDto The updated details of the project
     * @param companyId ID of the company that owns the project
     * @param projectId ID of the project to be updated
     * @return ResponseEntity containing the ProjectResponseDto of the updated project and HTTP status code OK (200)
     */
	@PatchMapping(path = "/{projectId}")
	public ResponseEntity<ProjectResponseDto> updateOneProject(
			@RequestBody ProjectRequestDto projectRequestDto,
			@PathVariable long companyId,
			@PathVariable long projectId
	) {
		return new ResponseEntity<ProjectResponseDto>(
				projectService.updateProject(projectRequestDto, companyId, projectId),
				HttpStatus.OK);
	}
	
    /**
     * Endpoint to delete a project in a company by its ID.
     *
     * @param companyId ID of the company that owns the project
     * @param projectId ID of the project to be deleted
     * @return ResponseEntity with HTTP status code NO CONTENT indicating successful deletion
     */
	@DeleteMapping(path = "/{projectId}")
	public ResponseEntity<Void> deleteOneProject(
			@PathVariable long companyId,
			@PathVariable long projectId
	) {
		projectService.deleteProject(companyId, projectId);
		return ResponseEntity.noContent().build();
	}
	
    /**
     * Endpoint to add a user to a project by its ID.
     *
     * @param companyId ID of the company that owns the project
     * @param projectId ID of the project where the user will be added
     * @param userId ID of the user to be added
     * @return ResponseEntity with HTTP status code NO CONTENT indicating successful operation
     */
	@PostMapping(path = "/{projectId}/add-user/{userId}")
	public ResponseEntity<Void> addOneUserToProject(
			@PathVariable long companyId,
			@PathVariable long projectId,
			@PathVariable long userId
	) {
		projectService.alterProjectMembersInProject(companyId, projectId, userId, true);
		return ResponseEntity.noContent().build();
	}
	
    /**
     * Endpoint to remove a user from a project by its ID.
     *
     * @param companyId ID of the company that owns the project
     * @param projectId ID of the project where the user will be removed
     * @param userId ID of the user to be removed
     * @return ResponseEntity with HTTP status code NO CONTENT indicating successful operation
     */
	@DeleteMapping(path = "/{projectId}/remove-user/{userId}")
	public ResponseEntity<Void> removeOneUserFromProject(
			@PathVariable long companyId,
			@PathVariable long projectId,
			@PathVariable long userId
	) {
		projectService.alterProjectMembersInProject(companyId, projectId, userId, false);
		return ResponseEntity.noContent().build();
	}
}
