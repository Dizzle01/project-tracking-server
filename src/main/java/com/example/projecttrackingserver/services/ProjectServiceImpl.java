package com.example.projecttrackingserver.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.projecttrackingserver.dto.ProjectRequestDto;
import com.example.projecttrackingserver.dto.ProjectResponseDto;
import com.example.projecttrackingserver.dto.UserResponseDto;
import com.example.projecttrackingserver.entities.CompanyEntity;
import com.example.projecttrackingserver.entities.ProjectEntity;
import com.example.projecttrackingserver.entities.UserEntity;
import com.example.projecttrackingserver.enums.Role;
import com.example.projecttrackingserver.exceptions.EntityAlreadyExistsException;
import com.example.projecttrackingserver.exceptions.EntityNotFoundException;
import com.example.projecttrackingserver.exceptions.UnauthorizedException;
import com.example.projecttrackingserver.exceptions.ValueNotAllowedException;
import com.example.projecttrackingserver.mappers.ProjectMapper;
import com.example.projecttrackingserver.mappers.UserMapper;
import com.example.projecttrackingserver.repositories.ProjectRepository;

import lombok.RequiredArgsConstructor;

/**
 * Implementation of {@link ProjectService} interface 
 */
@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

	private final ProjectRepository projectRepository;
	private final CompanyService companyService;
	private final ProjectMapper projectMapper;
	private final UserMapper userMapper;
	private final UserService userService;

    /**
     * {@inheritDoc}
     */
    public List<ProjectResponseDto> getAllProjectDtosInCompany(long companyId) {
    	// company does not exist -> deny
		if(!companyService.entityExists(companyId)) {
			throw new EntityNotFoundException("companyId", companyId);
		}
		
		return StreamSupport.stream(projectRepository.findAllByCompanyId(companyId).spliterator(), false)
							.map(project -> projectMapper.toDto(project))
							.collect(Collectors.toList());
    }
    
    /**
     * {@inheritDoc}
     */
    public ProjectResponseDto getProjectDtoInCompany(long companyId, long projectId) {
    	// company does not exist -> deny
		if(!companyService.entityExists(companyId)) {
			throw new EntityNotFoundException("companyId", companyId);
		}
		
		// project does not exist -> deny
		Optional<ProjectEntity> projectOptional = getEntityByIdAndCompanyId(projectId, companyId);
		if(projectOptional.isEmpty()) {
			throw new EntityNotFoundException("projectId", projectId);
		}
		
		return projectMapper.toDto(projectOptional.get());
    }
    
    /**
     * {@inheritDoc}
     */
    public List<UserResponseDto> getAllUserDtosInProject(long companyId, long projectId){
    	// company does not exist -> deny
		if(!companyService.entityExists(companyId)) {
			throw new EntityNotFoundException("companyId", companyId);
		}
		
		// project does not exist -> deny
		Optional<ProjectEntity> projectOptional = getEntityByIdAndCompanyId(projectId, companyId);
		if(projectOptional.isEmpty()) {
			throw new EntityNotFoundException("projectId", projectId);
		}

		return StreamSupport.stream(projectOptional.get().getMembers().spliterator(), false)
							.map(member -> userMapper.toDto(member))
							.collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
	public ProjectResponseDto createProject(ProjectRequestDto projectRequestDto, long companyId) {
		// requesting user is not admin of same company -> deny
		UserEntity requestingUser = (UserEntity)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(!(requestingUser.getRole().getRole() == Role.Admin && requestingUser.getCompany().getId() == companyId)) {
			throw new UnauthorizedException();
		}
		
		// company does not exist -> deny
		Optional<CompanyEntity> companyOptional = companyService.getEntityById(companyId);
		if(companyOptional.isEmpty()) {
			throw new EntityNotFoundException("companyId", companyId);
		}
		CompanyEntity companyEntity = companyOptional.get();
		
		// user does not exists -> deny
		Optional<UserEntity> userOptional = userService.getEntityByIdAndCompanyId(projectRequestDto.projectManagerId(), companyId);
		if(userOptional.isEmpty()) {
			throw new EntityNotFoundException("projectManagerId", projectRequestDto.projectManagerId());
		}
		UserEntity projectManagerEntity = userOptional.get();
		
		// user is not project manager -> deny
		if(projectManagerEntity.getRole().getRole() != Role.ProjectManager) {
			throw new UnauthorizedException(projectRequestDto.projectManagerId());
		}
		
		// project with same name already exists -> deny
		if(entityExists(projectRequestDto.projectName())) {
			throw new EntityAlreadyExistsException("projectName", projectRequestDto.projectName());
		}

		ProjectEntity projectEntity = projectMapper.toEntity(projectRequestDto, companyEntity, projectManagerEntity);
		
		projectEntity = projectRepository.save(projectEntity);
		
		return projectMapper.toDto(projectEntity);
	}
	
    /**
     * {@inheritDoc}
     */
	public void deleteProject(long companyId, long projectId) {
		// company does not exists -> deny
		if(!companyService.entityExists(companyId)) {
			throw new EntityNotFoundException("companyId", companyId);
		}
		
		// project does not exist -> deny
		Optional<ProjectEntity> projectOptional = getEntityByIdAndCompanyId(projectId, companyId);
		if(projectOptional.isEmpty()) {
			throw new EntityNotFoundException("projectId", projectId);
		}
		ProjectEntity projectEntity = projectOptional.get();
		
		// requesting user is not admin of same company -> deny
		UserEntity requestingUser = (UserEntity)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(!(requestingUser.getRole().getRole() == Role.Admin && requestingUser.getCompany().getId() == projectEntity.getCompany().getId())) {
			throw new UnauthorizedException();
		}
		
		projectRepository.delete(projectEntity);
	}
	
    /**
     * {@inheritDoc}
     */
	public ProjectResponseDto updateProject(ProjectRequestDto projectRequestDto, long companyId, long projectId) {
		// company does not exist -> deny
		if(!companyService.entityExists(companyId)) {
			throw new EntityNotFoundException("companyId", companyId);
		}
		
		// project to update does not exist -> deny
		Optional<ProjectEntity> projectToUpdateOptional = getEntityByIdAndCompanyId(projectId, companyId);
		if(projectToUpdateOptional.isEmpty()) {
			throw new EntityNotFoundException("projectId", projectId);
		}
		ProjectEntity projectToUpdateEntity = projectToUpdateOptional.get();
		
		// requesting user is not admin of same company -> deny
		UserEntity requestingUser = (UserEntity)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(!(requestingUser.getRole().getRole() == Role.Admin && requestingUser.getCompany().getId() == companyId)) {
			throw new UnauthorizedException();
		}
		
		// updated projectname is blank -> deny
		if(projectRequestDto.projectName().isBlank()) {
			throw new ValueNotAllowedException("projectName", projectRequestDto.projectName());
		}
		
		// other project with updated projectname already exists -> deny
		Optional<ProjectEntity> projectWithSameName = projectRepository.findByName(projectRequestDto.projectName());
		if(projectWithSameName.isPresent() && projectWithSameName.get().getId() != projectToUpdateEntity.getId()) {
			throw new EntityAlreadyExistsException("projectName", projectRequestDto.projectName());
		}
		
		// no project manager -> skip
		UserEntity projectManagerEntity = null;
		if(projectRequestDto.projectManagerId() != null) {
			// new project manager does not exist or is not project manager -> deny
			Optional<UserEntity> projectManagerOptional = userService.getEntityByIdAndCompanyId(projectRequestDto.projectManagerId(), companyId);
			if(projectManagerOptional.isEmpty() || projectManagerOptional.get().getRole().getRole() != Role.ProjectManager) {
				throw new EntityNotFoundException("projectManagerId", projectRequestDto.projectManagerId());
			}
			projectManagerEntity = projectManagerOptional.get();
		}

		projectToUpdateEntity = projectMapper.updateEntity(projectToUpdateEntity, projectRequestDto, projectManagerEntity);
		
		projectToUpdateEntity = projectRepository.save(projectToUpdateEntity);

		return projectMapper.toDto(projectToUpdateEntity);
	}
	
    /**
     * {@inheritDoc}
     */
	public void alterProjectMembersInProject(long companyId, long projectId, long userId, boolean addingUser) {
		// project does not exist -> deny
		Optional<ProjectEntity> projectOptional = getEntityByIdAndCompanyId(projectId, companyId);
		if(projectOptional.isEmpty()) {
			throw new EntityNotFoundException("projectId", projectId);
		}
		ProjectEntity projectEntity = projectOptional.get();
		
		// requesting user is not project manager of project -> deny
		UserEntity requestingUser = (UserEntity)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(!(requestingUser.getId() == projectEntity.getProjectManager().getId() && requestingUser.getRole().getRole() == Role.ProjectManager)) {
			throw new UnauthorizedException();
		}
		
		// user to add does not exist -> deny
		Optional<UserEntity> userOptional = userService.getEntityByIdAndCompanyId(userId, companyId);
		if(userOptional.isEmpty()) {
			throw new EntityNotFoundException("userId", userId);
		}
		UserEntity userEntity = userOptional.get();
		
		// requesting user is not in same company as the user to add -> deny
		if(requestingUser.getCompany().getId() != userOptional.get().getCompany().getId()) {
			throw new EntityNotFoundException("userId", userId);
		}

		if(addingUser) {
			// if user is already in project -> deny
			if(projectEntity.getMembers().contains(userEntity)) {
				throw new EntityAlreadyExistsException("userId", Long.toString(userId));
			}
			
			projectEntity.addUserToProject(userEntity);
		}
		else {
			// if user is not in project -> deny
			if(!projectEntity.getMembers().contains(userEntity)) {
				throw new EntityNotFoundException("userId", userId);
			}

			projectEntity.removeUserFromProject(userEntity);
		}
		
		projectRepository.save(projectEntity);
	}
	
    /**
     * {@inheritDoc}
     */
	public boolean entityExists(String projectName) {
		return projectRepository.findByName(projectName).isPresent();
	}
	
    /**
     * {@inheritDoc}
     */
	public boolean entityExists(long projectId, long companyId) {
		return projectRepository.findByIdAndCompanyId(projectId, companyId).isPresent();
	}
	
    /**
     * {@inheritDoc}
     */
	public Optional<ProjectEntity> getEntityByIdAndCompanyId(long projectId, long companyId) {
		return projectRepository.findByIdAndCompanyId(projectId, companyId);
	}
}
