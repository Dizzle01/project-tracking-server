package com.example.projecttrackingserver.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.projecttrackingserver.entities.UserEntity;

/**
 * Repository interface for performing CRUD operations on UserEntity.
 */
@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long>{

    /**
     * Finds all users belonging to a specific company.
     *
     * @param companyId the ID of the company
     * @return a list of UserEntity objects belonging to the specified company
     */
	public List<UserEntity> findAllByCompanyId(long companyId);
	
    /**
     * Finds a user by their ID and belonging to a specific company.
     *
     * @param id the ID of the user
     * @param companyId the ID of the company
     * @return an Optional containing the UserEntity if found, otherwise empty
     */
	public Optional<UserEntity> findByIdAndCompanyId(long id, long companyId);
	
    /**
     * Finds a user by their username.
     *
     * @param username the username of the user
     * @return an Optional containing the UserEntity if found, otherwise empty
     */
	public Optional<UserEntity> findByUsername(String username);
	
    /**
     * Finds a user by their API key.
     *
     * @param apiKey the API key of the user
     * @return an Optional containing the UserEntity if found, otherwise empty
     */
	public Optional<UserEntity> findByApiKey(String apiKey);
}