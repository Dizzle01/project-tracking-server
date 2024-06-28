package com.example.projecttrackingserver.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.projecttrackingserver.entities.RoleEntity;
import com.example.projecttrackingserver.enums.Role;

/**
 * Repository interface for performing CRUD operations on RoleEntity.
 */
@Repository
public interface RoleRepository extends CrudRepository<RoleEntity, Long> {

    /**
     * Retrieves an optional RoleEntity by its Role enumeration value.
     *
     * @param role the Role enumeration value to search for
     * @return an Optional containing the RoleEntity if found, otherwise empty
     */
	Optional<RoleEntity> findByRole(Role role);
}
