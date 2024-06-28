package com.example.projecttrackingserver.services;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.example.projecttrackingserver.TestDataUtil;
import com.example.projecttrackingserver.dto.ProjectRequestDto;
import com.example.projecttrackingserver.dto.ProjectResponseDto;
import com.example.projecttrackingserver.dto.UserResponseDto;
import com.example.projecttrackingserver.entities.CompanyEntity;
import com.example.projecttrackingserver.entities.ProjectEntity;
import com.example.projecttrackingserver.entities.RoleEntity;
import com.example.projecttrackingserver.entities.UserEntity;
import com.example.projecttrackingserver.exceptions.EntityAlreadyExistsException;
import com.example.projecttrackingserver.exceptions.EntityNotFoundException;
import com.example.projecttrackingserver.exceptions.UnauthorizedException;
import com.example.projecttrackingserver.exceptions.ValueNotAllowedException;
import com.example.projecttrackingserver.mappers.ProjectMapper;
import com.example.projecttrackingserver.mappers.ProjectMapperImpl;
import com.example.projecttrackingserver.mappers.UserMapper;
import com.example.projecttrackingserver.mappers.UserMapperImpl;
import com.example.projecttrackingserver.repositories.ProjectRepository;

/**
 * Test class for the ProjectServiceImpl.
 * This class tests if the ProjectService functionality works as intended.
 */
@ExtendWith(MockitoExtension.class)
public class ProjectServiceTests {

	@InjectMocks
	private ProjectServiceImpl underTest;
	
	@Mock
	private ProjectRepository projectRepository;
	
	@Mock
	private CompanyService companyService;
	
	@Spy
	private ProjectMapper projectMapper = new ProjectMapperImpl();
	
	@Spy
	private UserMapper userMapper = new UserMapperImpl();
	
	@Mock
	private UserService userService;
	
    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;
	
	private ProjectEntity projectMock;
	private ProjectEntity projectEntity1;
	private ProjectEntity projectEntity2;
	private CompanyEntity companyEntity;
	private UserEntity projectManagerEntity;
	private UserEntity requestingAdminEntity;
	private RoleEntity projectManagerRoleEntity;
	private long validCompanyId;
	private long invalidCompanyId;
	private long validProjectId;
	private long invalidProjectId;
	private long validUserId;
	private long invalidUserId;
	private String notExistingProjectname;
	private String alreadyExistingProjectname;
	private String blankProjectname;
	private String updatedProjectname;
	@BeforeEach
	public void setUp() {
		// Arrange
		companyEntity = TestDataUtil.createCompany1();
		RoleEntity adminRoleEntity = TestDataUtil.createAdminRole();
		projectManagerRoleEntity = TestDataUtil.createProjectManagerRole();
		projectManagerEntity = TestDataUtil.createUser1(projectManagerRoleEntity, companyEntity);
		requestingAdminEntity = TestDataUtil.createUser1(adminRoleEntity, companyEntity);
		projectMock = Mockito.mock(ProjectEntity.class);
		projectEntity1 = TestDataUtil.createProject1(companyEntity, projectManagerEntity);
		projectEntity2 = TestDataUtil.createProject2(companyEntity, projectManagerEntity);
		validCompanyId = companyEntity.getId();
		invalidCompanyId = 100;
		validProjectId = projectEntity1.getId();
		invalidProjectId = 100;
		validUserId = projectManagerEntity.getId();
		invalidUserId = 100;
		notExistingProjectname = "NotExistingUsername";
		alreadyExistingProjectname = "ExistingUsername";
		blankProjectname = "";
		updatedProjectname = "UpdatedProjectname";
		SecurityContextHolder.setContext(securityContext);
	}
	
    /**
     * Tests retrieving multiple projects.
     * Expects that multiple projects are successfully mapped and retrieved.
     */
	@Test
	public void getAllProjectsDtosInCompany_RetrieveMultipleProjects_ReturnMultipleProjectDtos() {
		// Arrange
		List<ProjectEntity> projects = Arrays.asList(projectEntity1, projectEntity2);
		
		// Mock
		when(companyService.entityExists(validCompanyId))
		   				   .thenReturn(true);
		when(companyService.entityExists(invalidCompanyId))
						   .thenReturn(false);
		when(projectRepository.findAllByCompanyId(validCompanyId))
							  .thenReturn(projects);
		
		// Act
		List<ProjectResponseDto> projectResponseDtos = underTest.getAllProjectDtosInCompany(validCompanyId);
		
		// Assert
		assertAll(() -> {
			assertEquals(projects.size(), projectResponseDtos.size());
			assertThrows(EntityNotFoundException.class, () -> underTest.getAllProjectDtosInCompany(invalidCompanyId));
		});
		for(ProjectEntity project : projects) {
			verify(projectMapper, times(1)).toDto(project);
		}
		verify(projectRepository, times(1)).findAllByCompanyId(validCompanyId);
	}
	
    /**
     * Tests retrieving one project.
     * Expects that one project is successfully mapped and retrieved.
     */
	@Test
	public void getProjectDtoInCompany_RetrieveOneProject_ReturnOneProjectDto() {
		// Mock
		when(companyService.entityExists(validCompanyId))
		   				   .thenReturn(true);
		when(companyService.entityExists(invalidCompanyId))
		   				   .thenReturn(false);
		when(projectRepository.findByIdAndCompanyId(validProjectId, validCompanyId))
	   	  					  .thenReturn(Optional.of(projectEntity1));
		when(projectRepository.findByIdAndCompanyId(invalidProjectId, validCompanyId))
					  		  .thenReturn(Optional.empty());
		
		// Act
		ProjectResponseDto projectResponseDto = underTest.getProjectDtoInCompany(validCompanyId, validProjectId);
		
		// Assert
		assertAll(() -> {
			assertEquals(projectEntity1.getId(), projectResponseDto.id());
			assertEquals(projectEntity1.getName(), projectResponseDto.projectName());
			assertEquals(projectEntity1.getDescription(), projectResponseDto.description());
			assertEquals(projectEntity1.getStartDate(), projectResponseDto.startDate());
			assertEquals(projectEntity1.getEndDate(), projectResponseDto.endDate());
			assertEquals(projectEntity1.getProjectManager().getId(), projectResponseDto.projectManagerId());
			assertThrows(EntityNotFoundException.class, () -> underTest.getProjectDtoInCompany(invalidCompanyId, validProjectId));
			assertThrows(EntityNotFoundException.class, () -> underTest.getProjectDtoInCompany(validCompanyId, invalidProjectId));
		});
		verify(projectMapper, times(1)).toDto(projectEntity1);
		verify(projectRepository, times(1)).findByIdAndCompanyId(validProjectId, validCompanyId);
	}
	
    /**
     * Tests retrieving multiple users in a project.
     * Expects that multiple users are successfully mapped and retrieved.
     */
	@Test
	public void getAllUserDtosInProject_RetrieveMultipleUsers_ReturnMultipleUserDtos() {
		// Arrange
		projectEntity1.setMembers(Arrays.asList(projectManagerEntity, requestingAdminEntity));
		
		// Mock
		when(companyService.entityExists(validCompanyId))
		   				   .thenReturn(true);
		when(companyService.entityExists(invalidCompanyId))
		   				   .thenReturn(false);
		when(projectRepository.findByIdAndCompanyId(validProjectId, validCompanyId))
	   	  					  .thenReturn(Optional.of(projectEntity1));
		when(projectRepository.findByIdAndCompanyId(invalidProjectId, validCompanyId))
					  		  .thenReturn(Optional.empty());
		
		// Act
		List<UserResponseDto> userResponseDtos = underTest.getAllUserDtosInProject(validCompanyId, validProjectId);
		
		assertAll(() -> {
			assertEquals(projectEntity1.getMembers().size(), userResponseDtos.size());
			assertThrows(EntityNotFoundException.class, () -> underTest.getAllUserDtosInProject(invalidCompanyId, validProjectId));
			assertThrows(EntityNotFoundException.class, () -> underTest.getAllUserDtosInProject(validCompanyId, invalidProjectId));
		});
		for(UserEntity user : projectEntity1.getMembers()) {
			verify(userMapper, times(1)).toDto(user);
		}
		verify(projectRepository, times(1)).findByIdAndCompanyId(validProjectId, validCompanyId);
	}
	
    /**
     * Tests creating and retrieving one project.
     * Expects that one project is successfully saved, mapped and retrieved.
     */
	@Test
	public void createProject_CreateProject_ReturnOneProjectDto() {
		// Arrange
		ProjectRequestDto projectRequestDto1 = new ProjectRequestDto(notExistingProjectname, "description", "01-01-1010", "02-02-2020", projectManagerEntity.getId());
		ProjectRequestDto projectRequestDto2 = new ProjectRequestDto(alreadyExistingProjectname, "descritpion", "01-01-1010", "02-02-2020", projectManagerEntity.getId());
		
		// Mock
		when(companyService.getEntityById(validCompanyId))
		   				   .thenReturn(Optional.of(companyEntity));
		when(userService.getEntityByIdAndCompanyId(validUserId, validUserId))
					   	.thenReturn(Optional.of(projectManagerEntity));
		when(projectRepository.save(Mockito.any(ProjectEntity.class)))
		   					  .thenReturn(projectEntity1);
		when(projectRepository.findByName(notExistingProjectname))
							  .thenReturn(Optional.empty());
		when(projectRepository.findByName(alreadyExistingProjectname))
		  					  .thenReturn(Optional.of(projectMock));
		when(securityContext.getAuthentication())
							.thenReturn(authentication);
		when(authentication.getPrincipal())
			   			   .thenReturn(requestingAdminEntity);
		
		// Act
		ProjectResponseDto projectResponseDto = underTest.createProject(projectRequestDto1, validCompanyId);
		
		// Assert
		assertAll(() -> {
			assertNotNull(projectResponseDto);
			assertEquals(projectEntity1.getId(), projectResponseDto.id());
			assertEquals(projectEntity1.getName(), projectResponseDto.projectName());
			assertEquals(projectEntity1.getDescription(), projectResponseDto.description());
			assertEquals(projectEntity1.getStartDate(), projectResponseDto.startDate());
			assertEquals(projectEntity1.getEndDate(), projectResponseDto.endDate());
			assertEquals(projectEntity1.getProjectManager().getId(), projectResponseDto.projectManagerId());
			assertThrows(UnauthorizedException.class, () -> underTest.createProject(projectRequestDto1, invalidCompanyId));
			assertThrows(EntityAlreadyExistsException.class, () -> underTest.createProject(projectRequestDto2, validCompanyId));
		});
		verify(projectMapper, times(1)).toEntity(projectRequestDto1, companyEntity, projectManagerEntity);
		verify(projectRepository, times(1)).save(Mockito.any(ProjectEntity.class));
		verify(projectMapper, times(1)).toDto(projectEntity1);
	}
	
    /**
     * Tests removing one project.
     * Expects that one project is successfully deleted.
     */
	@Test
	public void deleteProject_RemoveProject_ReturnNothing() {
		// Mock
		when(companyService.entityExists(validCompanyId))
		   				   .thenReturn(true);
		when(companyService.entityExists(invalidCompanyId))
		   				   .thenReturn(false);
		when(projectRepository.findByIdAndCompanyId(validProjectId, validCompanyId))
			  			   .thenReturn(Optional.of(projectEntity1));
		when(projectRepository.findByIdAndCompanyId(invalidProjectId, validCompanyId))
		  				   .thenReturn(Optional.empty());
		when(securityContext.getAuthentication())
						    .thenReturn(authentication);
		when(authentication.getPrincipal())
						   .thenReturn(requestingAdminEntity);
		
		// Act
		underTest.deleteProject(validCompanyId, validProjectId);
		
		// Assert
		assertAll(() -> {
			assertThrows(EntityNotFoundException.class, () -> underTest.deleteProject(invalidCompanyId, validProjectId));
			assertThrows(EntityNotFoundException.class, () -> underTest.deleteProject(validCompanyId, invalidProjectId));
		});
		verify(projectRepository, times(1)).delete(projectEntity1);
	}
	
    /**
     * Tests updating one project.
     * Expects that one project is successfully updated,mapped and retrieved.
     */
	@Test
	public void updateProject_UpdateProjectFields_ReturnOneProjectDto() {
		// Arrange
		String startDateString = "01-01-1010";
		String endDateString = "02-02-2020";
		ProjectRequestDto projectRequestDto1 = new ProjectRequestDto(updatedProjectname, "UpdatedDescription", startDateString, endDateString, projectManagerEntity.getId());
		ProjectRequestDto projectRequestDto2 = new ProjectRequestDto(alreadyExistingProjectname, "descritpion", startDateString, endDateString, projectManagerEntity.getId());
		ProjectRequestDto projectRequestDto3 = new ProjectRequestDto(blankProjectname, "descritpion", startDateString, endDateString, projectManagerEntity.getId());
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		LocalDate startDate = LocalDate.parse(startDateString, formatter);
		LocalDate endDate = LocalDate.parse(endDateString, formatter);
		
		// Mock
		when(companyService.entityExists(validCompanyId))
		   				   .thenReturn(true);
		when(companyService.entityExists(invalidCompanyId))
		   				   .thenReturn(false);
		when(projectRepository.findByIdAndCompanyId(validProjectId, validCompanyId))
		   					  .thenReturn(Optional.of(projectEntity1));
		when(projectRepository.findByIdAndCompanyId(invalidProjectId, validCompanyId))
			  				  .thenReturn(Optional.empty());
		when(projectRepository.findByName(updatedProjectname))
						 	  .thenReturn(Optional.empty());
		when(projectRepository.findByName(alreadyExistingProjectname))
		   					  .thenReturn(Optional.of(projectMock));
		when(projectRepository.save(Mockito.any(ProjectEntity.class)))
	 	  					  .then(AdditionalAnswers.returnsFirstArg());
		when(securityContext.getAuthentication())
							.thenReturn(authentication);
        when(authentication.getPrincipal())
        				   .thenReturn(requestingAdminEntity);
		when(userService.getEntityByIdAndCompanyId(projectManagerEntity.getId(), validCompanyId))
				   		.thenReturn(Optional.of(projectManagerEntity));

		// Act
		ProjectResponseDto projectResponseDto = underTest.updateProject(projectRequestDto1, validCompanyId, validProjectId);
		
		// Assert
		assertAll(() -> {
			assertEquals(projectEntity1.getId(), projectResponseDto.id());
			assertEquals(projectRequestDto1.projectName(), projectResponseDto.projectName());
			assertEquals(projectRequestDto1.description(), projectResponseDto.description());
			assertEquals(startDate, projectResponseDto.startDate());
			assertEquals(endDate, projectResponseDto.endDate());
			assertEquals(projectRequestDto1.projectManagerId(), projectResponseDto.projectManagerId());
			assertThrows(EntityNotFoundException.class, () -> underTest.updateProject(projectRequestDto1, invalidCompanyId, validUserId));
			assertThrows(EntityNotFoundException.class, () -> underTest.updateProject(projectRequestDto1, validCompanyId, invalidUserId));
			assertThrows(EntityAlreadyExistsException.class, () -> underTest.updateProject(projectRequestDto2, validCompanyId, validUserId));
			assertThrows(ValueNotAllowedException.class, () -> underTest.updateProject(projectRequestDto3, validCompanyId, validUserId));
		});
		verify(projectMapper, times(1)).updateEntity(projectEntity1, projectRequestDto1, projectManagerEntity);
		verify(projectRepository, times(1)).save(Mockito.any(ProjectEntity.class));
		verify(projectMapper, times(1)).toDto(projectEntity1);
	}
}
