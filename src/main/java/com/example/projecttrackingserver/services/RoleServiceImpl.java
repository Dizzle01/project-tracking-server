package com.example.projecttrackingserver.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.projecttrackingserver.entities.RoleEntity;
import com.example.projecttrackingserver.enums.Role;
import com.example.projecttrackingserver.repositories.RoleRepository;

import lombok.RequiredArgsConstructor;

/**
 * Implementation of {@link RoleService} interface 
 */
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

	private final RoleRepository roleRepository;
	
    /**
     * {@inheritDoc}
     */
	public Optional<RoleEntity> getEntityByRole(Role Role) {
		return roleRepository.findByRole(Role);
	}
	
    /**
     * {@inheritDoc}
     */
	public Optional<RoleEntity> getEntityById(long roleId) {
		return roleRepository.findById(roleId);
	}
}
