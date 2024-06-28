package com.example.projecttrackingserver.services;

import java.util.Optional;

import com.example.projecttrackingserver.entities.RoleEntity;
import com.example.projecttrackingserver.enums.Role;

/**
 * Service interface related to roles.
 */
public interface RoleService {

    /**
     * Retrieves a RoleEntity by its Role enum value.
     *
     * @param role the Role enum value
     * @return an Optional containing the RoleEntity if found, otherwise empty
     */
	Optional<RoleEntity> getEntityByRole(Role Role);
	
    /**
     * Retrieves a RoleEntity by its ID.
     *
     * @param roleId the ID of the role
     * @return an Optional containing the RoleEntity if found, otherwise empty
     */
	Optional<RoleEntity> getEntityById(long roleId);
}
