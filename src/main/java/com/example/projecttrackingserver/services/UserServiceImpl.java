package com.example.projecttrackingserver.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.projecttrackingserver.dto.UserRequestDto;
import com.example.projecttrackingserver.dto.UserResponseDto;
import com.example.projecttrackingserver.entities.CompanyEntity;
import com.example.projecttrackingserver.entities.RoleEntity;
import com.example.projecttrackingserver.entities.UserEntity;
import com.example.projecttrackingserver.enums.Role;
import com.example.projecttrackingserver.exceptions.EntityAlreadyExistsException;
import com.example.projecttrackingserver.exceptions.EntityNotFoundException;
import com.example.projecttrackingserver.exceptions.UnauthorizedException;
import com.example.projecttrackingserver.exceptions.ValueNotAllowedException;
import com.example.projecttrackingserver.mappers.UserMapper;
import com.example.projecttrackingserver.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

/**
 * Implementation of {@link UserService} interface 
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final UserMapper userMapper;
	private final RoleService roleService;
	private final CompanyService companyService;
	
    /**
     * {@inheritDoc}
     */
	public List<UserResponseDto> getAllUserDtosByCompanyId(long companyId) {
		// company does not exist -> deny
		if(!companyService.entityExists(companyId)) {
			throw new EntityNotFoundException("companyId", companyId);
		}
		
		return StreamSupport.stream(userRepository.findAllByCompanyId(companyId).spliterator(), false)
							.map(user -> userMapper.toDto(user))
							.collect(Collectors.toList());
	}
	
    /**
     * {@inheritDoc}
     */
	public UserResponseDto getUserDtoById(long companyId, long userId) {
		// company does not exist -> deny
		if(!companyService.entityExists(companyId)) {
			throw new EntityNotFoundException("companyId", companyId);
		}
		
		// user does not exist in company -> deny
		Optional<UserEntity> userOptional = getEntityByIdAndCompanyId(userId, companyId);
		if(userOptional.isEmpty()) {
			throw new EntityNotFoundException("userId", userId);
		}
		UserEntity userEntity = userOptional.get();
			
		return userMapper.toDto(userEntity);
	}
	
    /**
     * {@inheritDoc}
     */
	public UserResponseDto createUser(UserRequestDto userRequestDto, long companyId) {
		// company does not exist -> deny
		Optional<CompanyEntity> companyOptional = companyService.getEntityById(companyId);
		if(companyOptional.isEmpty()) {
			throw new EntityNotFoundException("companyId", companyId);
		}
		
		// user with username already exists -> deny
		if(entityExists(userRequestDto.username())) {
			throw new EntityAlreadyExistsException("username", userRequestDto.username());
		}
		
		// developer role does not exists -> deny
		Optional<RoleEntity> roleOptional = roleService.getEntityByRole(Role.Developer);
		if(roleOptional.isEmpty()) {
			throw new EntityNotFoundException("role", Role.Developer.toString());
		}
		
		UserEntity userEntity = userMapper.toEntity(userRequestDto, companyOptional.get(), roleOptional.get());

		userEntity = userRepository.save(userEntity);
		
		return userMapper.toDto(userEntity);
	}
	
    /**
     * {@inheritDoc}
     */
	public UserResponseDto updateUser(UserRequestDto userRequestDto, long companyId, long userId) {
		// company does not exist -> deny
		if(!companyService.entityExists(companyId)) {
			throw new EntityNotFoundException("companyId", companyId);
		}
		
		// user to update does not exist -> deny
		Optional<UserEntity> userToUpdateOptional = getEntityByIdAndCompanyId(userId, companyId);
		if(userToUpdateOptional.isEmpty()) {
			throw new EntityNotFoundException("userId", userId);
		}
		UserEntity userToUpdateEntity = userToUpdateOptional.get();
		
		// requesting user is not user to update or not admin in same company as user to update -> deny
		UserEntity requestingUser = (UserEntity)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(!(requestingUser.getId() == userId || requestingUser.getRole().getRole() == Role.Admin && requestingUser.getCompany().getId() == userToUpdateEntity.getCompany().getId())) {
			throw new UnauthorizedException();
		}
		
		// username is blank -> deny
		if(userRequestDto.username().isBlank()) {
			throw new ValueNotAllowedException("username", userRequestDto.username());
		}
		
		// other user with updated username already exists -> deny
		if(entityExists(userRequestDto.username())) {
			throw new EntityAlreadyExistsException("username", userRequestDto.username());
		}
		
		userToUpdateEntity = userMapper.updateEntity(userToUpdateEntity, userRequestDto);
		
		userToUpdateEntity = userRepository.save(userToUpdateEntity);

		return userMapper.toDto(userToUpdateEntity);
	}
	
    /**
     * {@inheritDoc}
     */
	public UserResponseDto assignRoleToUser(long companyId, long userId, long roleId) {
		// company does not exist -> deny
		if(!companyService.entityExists(companyId)) {
			throw new EntityNotFoundException("companyId", companyId);
		}

		// user to assign role does not exist -> deny
		Optional<UserEntity> userToAssignRoleOptional = getEntityByIdAndCompanyId(userId, companyId);
		if(userToAssignRoleOptional.isEmpty()) {
			throw new EntityNotFoundException("userId", userId);
		}
		UserEntity userToAssignRoleEntity = userToAssignRoleOptional.get();
		
		// requesting user is not user to update or not admin in same company as user to update -> deny
		UserEntity requestingUser = (UserEntity)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(!(requestingUser.getId() == userId || requestingUser.getRole().getRole() == Role.Admin && requestingUser.getCompany().getId() == userToAssignRoleEntity.getCompany().getId())) {
			throw new UnauthorizedException();
		}
		
		// role does not exist -> deny
		Optional<RoleEntity> roleOptional = roleService.getEntityById(roleId);
		if(roleOptional.isEmpty()) {
			throw new EntityNotFoundException("roleId", roleId);
		}
		RoleEntity roleEntity = roleOptional.get();
		
		userToAssignRoleEntity.setRole(roleEntity);
		userToAssignRoleEntity = userRepository.save(userToAssignRoleEntity);
		
		return userMapper.toDto(userToAssignRoleEntity);
	}
	
    /**
     * {@inheritDoc}
     */
	public void deleteUser(long companyId, long userId) {
		// company does not exists -> deny
		if(!companyService.entityExists(companyId)) {
			throw new EntityNotFoundException("companyId", companyId);
		}
		
		// user does not exists -> deny
		Optional<UserEntity> userToDeleteOptional = getEntityByIdAndCompanyId(userId, companyId);
		if(userToDeleteOptional.isEmpty()) {
			throw new EntityNotFoundException("userId", userId);
		}
		UserEntity userToDeleteEntity = userToDeleteOptional.get();
		
		// requesting user is not user to delete or not admin of same company -> deny
		UserEntity requestingUser = (UserEntity)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(!(requestingUser.getId() == userId || requestingUser.getRole().getRole() == Role.Admin && requestingUser.getCompany().getId() == userToDeleteEntity.getCompany().getId())) {
			throw new UnauthorizedException();
		}
		
		userRepository.deleteById(userId);
	}
	
    /**
     * {@inheritDoc}
     */
	public Optional<UserEntity> getEntityByAPIKey(String apiKey) {
		return userRepository.findByApiKey(apiKey);
	}
	
    /**
     * {@inheritDoc}
     */
	public Optional<UserEntity> getEntityByIdAndCompanyId(long userId, long companyId) {
		return userRepository.findByIdAndCompanyId(userId, companyId);
	}
	
    /**
     * {@inheritDoc}
     */
	public boolean entityExists(String username) {
		return userRepository.findByUsername(username).isPresent();
	}
}
