package com.example.projecttrackingserver.services;

import java.util.List;
import java.util.Optional;

import com.example.projecttrackingserver.dto.UserRequestDto;
import com.example.projecttrackingserver.dto.UserResponseDto;
import com.example.projecttrackingserver.entities.UserEntity;

/**
 * Service interface related to users.
 */
public interface UserService {

    /**
     * Retrieves a list of UserResponseDto for all users in a company.
     *
     * @param companyId the ID of the company
     * @return a list of UserResponseDto objects
     */
	List<UserResponseDto> getAllUserDtosByCompanyId(long companyId);
	
    /**
     * Retrieves a UserResponseDto by user ID within a company.
     *
     * @param companyId the ID of the company
     * @param userId    the ID of the user
     * @return the UserResponseDto if found, otherwise empty
     */
	UserResponseDto getUserDtoById(long companyId, long userId);
	
    /**
     * Creates a new user and returns the created UserResponseDto.
     *
     * @param userRequestDto the UserRequestDto containing user data
     * @param companyId      the ID of the company to which the user belongs
     * @return the created UserResponseDto
     */
	UserResponseDto createUser(UserRequestDto userRequestDto, long companyId);
	
    /**
     * Updates an existing user and returns the updated UserResponseDto.
     *
     * @param userRequestDto the UserRequestDto containing updated user data
     * @param companyId      the ID of the company to which the user belongs
     * @param userId         the ID of the user to update
     * @return the updated UserResponseDto
     */
	UserResponseDto updateUser(UserRequestDto userRequestDto, long companyId, long userId);
	
    /**
     * Assigns a role to a user and returns the updated UserResponseDto.
     *
     * @param companyId the ID of the company to which the user belongs
     * @param userId    the ID of the user to update
     * @param roleId    the ID of the role to assign
     * @return the updated UserResponseDto
     */
	UserResponseDto assignRoleToUser(long companyId, long userId, long roleId);
	
    /**
     * Deletes a user by user ID within a company.
     *
     * @param companyId the ID of the company
     * @param userId    the ID of the user to delete
     */
	void deleteUser(long companyId, long userId);
	
    /**
     * Retrieves a user entity by API key.
     *
     * @param apiKey the API key associated with the user
     * @return an Optional containing the UserEntity if found, otherwise empty
     */
	Optional<UserEntity> getEntityByAPIKey(String apiKey);
	
    /**
     * Retrieves a user entity by user ID and company ID.
     *
     * @param userId    the ID of the user
     * @param companyId the ID of the company
     * @return an Optional containing the UserEntity if found, otherwise empty
     */
	Optional<UserEntity> getEntityByIdAndCompanyId(long userId, long companyId);
	
    /**
     * Checks if a user entity exists by username.
     *
     * @param username the username to check
     * @return true if the user entity exists, false otherwise
     */
	boolean entityExists(String username);
}
