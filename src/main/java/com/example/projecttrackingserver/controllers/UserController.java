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

import com.example.projecttrackingserver.dto.UserRequestDto;
import com.example.projecttrackingserver.dto.UserResponseDto;
import com.example.projecttrackingserver.services.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Controller class for handling user-related HTTP requests.
 */
@RestController
@RequestMapping("/api/v1/companies/{companyId}/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;
	
    /**
     * Endpoint to retrieve all users in a company.
     *
     * @param companyId ID of the company to retrieve users for
     * @return ResponseEntity containing a list of UserResponseDto and HTTP status code OK (200)
     */
	@GetMapping
	public ResponseEntity<List<UserResponseDto>> getAllUsersInCompany(
			@PathVariable long companyId
	) {
		return new ResponseEntity<List<UserResponseDto>>(
				userService.getAllUserDtosByCompanyId(companyId),
				HttpStatus.OK);
	}

    /**
     * Endpoint to retrieve a specific user in a company by its ID.
     *
     * @param companyId ID of the company that owns the user
     * @param userId ID of the user to retrieve
     * @return ResponseEntity containing the UserResponseDto and HTTP status code OK (200)
     */
	@GetMapping(path = "/{userId}")
	public ResponseEntity<UserResponseDto> getOneUserInCompany(
			@PathVariable long companyId,
			@PathVariable long userId
	) {
		return new ResponseEntity<UserResponseDto>(
				userService.getUserDtoById(companyId, userId),
				HttpStatus.OK);
	}
	
    /**
     * Endpoint to create a new user in a company.
     *
     * @param userRequestDto The details of the user to be created
     * @param companyId ID of the company where the user will be created
     * @return ResponseEntity containing the UserResponseDto of the created user and HTTP status code CREATED (201)
     */
	@PostMapping
	public ResponseEntity<UserResponseDto> createOneUser(
			@Valid @RequestBody UserRequestDto userRequestDto,
			@PathVariable long companyId
	) {
		return new ResponseEntity<UserResponseDto>(
				userService.createUser(userRequestDto, companyId),
				HttpStatus.CREATED);
	}
	
    /**
     * Endpoint to updates a user in a company by its ID.
     *
     * @param userRequestDto The updated details of the user
     * @param companyId ID of the company that owns the user
     * @param userId ID of the user to be updated
     * @return ResponseEntity containing the UserResponseDto of the updated user and HTTP status code OK (200)
     */
	@PatchMapping(path = "/{userId}")
	public ResponseEntity<UserResponseDto> updateOneUser(
			@Valid @RequestBody UserRequestDto userRequestDto,
			@PathVariable long companyId,
			@PathVariable long userId
	) {
		return new ResponseEntity<UserResponseDto>(
				userService.updateUser(userRequestDto, companyId, userId),
				HttpStatus.OK);
	}
	
    /**
     * Endpoint to assign a role to a user by its ID.
     *
     * @param companyId ID of the company that owns the user
     * @param userId ID of the user to assign the role to
     * @param roleId ID of the role to assign
     * @return ResponseEntity containing the UserResponseDto of the updated user with assigned role and HTTP status code OK (200)
     */
	@PatchMapping(path = "/{userId}/assign-role/{roleId}")
	public ResponseEntity<UserResponseDto> assignRoleToUser(
			@PathVariable long companyId,
			@PathVariable long userId,
			@PathVariable long roleId
	) {
		return new ResponseEntity<UserResponseDto>(
				userService.assignRoleToUser(companyId, userId, roleId),
				HttpStatus.OK);
	}
	
    /**
     * Endpoint to delete a user from a company by its ID.
     *
     * @param companyId ID of the company that owns the user
     * @param userId ID of the user to be deleted
     * @return ResponseEntity with HTTP status code NO CONTENT indicating successful deletion
     */
	@DeleteMapping(path = "/{userId}")
	public ResponseEntity<Void> deleteOneUser(
			@PathVariable long companyId,
			@PathVariable long userId
	) {
		userService.deleteUser(companyId, userId);
		return ResponseEntity.noContent().build();
	}
}
