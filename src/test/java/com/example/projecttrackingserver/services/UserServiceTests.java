package com.example.projecttrackingserver.services;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import com.example.projecttrackingserver.dto.UserRequestDto;
import com.example.projecttrackingserver.dto.UserResponseDto;
import com.example.projecttrackingserver.entities.CompanyEntity;
import com.example.projecttrackingserver.entities.RoleEntity;
import com.example.projecttrackingserver.entities.UserEntity;
import com.example.projecttrackingserver.enums.Role;
import com.example.projecttrackingserver.exceptions.EntityAlreadyExistsException;
import com.example.projecttrackingserver.exceptions.EntityNotFoundException;
import com.example.projecttrackingserver.exceptions.ValueNotAllowedException;
import com.example.projecttrackingserver.mappers.UserMapper;
import com.example.projecttrackingserver.mappers.UserMapperImpl;
import com.example.projecttrackingserver.repositories.UserRepository;

/**
 * Test class for the UserServiceImpl.
 * This class tests if the UserService functionality works as intended.
 */
@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

	@InjectMocks
	private UserServiceImpl underTest;
	
	@Mock
	private UserRepository userRepository;
	
	@Mock
	private CompanyService companyService;
	
	@Mock
	private RoleService roleService;
	
    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;
	
	@Spy
	private UserMapper userMapper = new UserMapperImpl();
	
	private UserEntity userMock;
	private UserEntity requestingAdminEntity;
	private UserEntity userEntity1;
	private UserEntity userEntity2;
	private CompanyEntity companyEntity;
	private RoleEntity developerRoleEntity;
	private long validCompanyId;
	private long invalidCompanyId;
	private long validUserId;
	private long invalidUserId;
	private long validRoleId;
	private long invalidRoleId;
	private String notExistingUsername;
	private String alreadyExistingUsername;
	private String blankUsername;
	@BeforeEach
	public void setUp() {
		// Arrange
		userMock = Mockito.mock(UserEntity.class);
		companyEntity = TestDataUtil.createCompany1();
		developerRoleEntity = TestDataUtil.createDeveloperRole();
		RoleEntity adminRoleEntity = TestDataUtil.createAdminRole();
		requestingAdminEntity = TestDataUtil.createUser1(adminRoleEntity, companyEntity);
		userEntity1 = TestDataUtil.createUser1(developerRoleEntity, companyEntity);
		userEntity2 = TestDataUtil.createUser2(developerRoleEntity, companyEntity);
		validCompanyId = companyEntity.getId();
		invalidCompanyId = 100;
		validUserId = userEntity1.getId();
		invalidUserId = 100;
		validRoleId = developerRoleEntity.getId();
		invalidRoleId = 100;
		notExistingUsername = "NotExistingUsername";
		alreadyExistingUsername = "ExistingUsername";
		blankUsername = "";
		SecurityContextHolder.setContext(securityContext);
	}
	
    /**
     * Tests retrieving multiple users in a company.
     * Expects that multiple users are successfully mapped and retrieved.
     */
	@Test
	public void getAllUserDtosByCompanyId_RetrieveMultipleUser_ReturnMultipleUserDtos() {
		// Arrange
		List<UserEntity> users = Arrays.asList(userEntity1, userEntity2);
		
		// Mock
		when(companyService.entityExists(validCompanyId))
		   				   .thenReturn(true);
		when(companyService.entityExists(invalidCompanyId))
		   				   .thenReturn(false);
		when(userRepository.findAllByCompanyId(validCompanyId))
		  				   .thenReturn(users);
		
		// Act
		List<UserResponseDto> userResponseDtos = underTest.getAllUserDtosByCompanyId(validCompanyId);
		
		// Assert
		assertAll(() -> {
			assertEquals(users.size(), userResponseDtos.size());
			assertThrows(EntityNotFoundException.class, () -> underTest.getAllUserDtosByCompanyId(invalidCompanyId));
		});
		for(UserEntity user : users) {
			verify(userMapper, times(1)).toDto(user);
		}
		verify(userRepository, times(1)).findAllByCompanyId(validCompanyId);
	}
	
    /**
     * Tests retrieving one user in a company.
     * Expects that one user is successfully mapped and retrieved.
     */
	@Test
	public void getUserDtoById_RetrieveOneUser_ReturnOneUserDto() {
		// Mock
		when(companyService.entityExists(validCompanyId))
		   				   .thenReturn(true);
		when(companyService.entityExists(invalidCompanyId))
		   				   .thenReturn(false);
		when(userRepository.findByIdAndCompanyId(validUserId, validCompanyId))
						   .thenReturn(Optional.of(userEntity1));
		
		// Act
		UserResponseDto userResponseDto = underTest.getUserDtoById(validUserId, validCompanyId);
		
		// Assert
		assertAll(() -> {
			assertEquals(userEntity1.getId(), userResponseDto.id());
			assertEquals(userEntity1.getUsername(), userResponseDto.username());
			assertEquals(userEntity1.getCompany().getName(), userResponseDto.company());
			assertEquals(userEntity1.getRole().getRole(), userResponseDto.role());
			assertEquals(userEntity1.getApiKey(), userResponseDto.apiKey());
			assertThrows(EntityNotFoundException.class, () -> underTest.getUserDtoById(invalidUserId, validCompanyId));
			assertThrows(EntityNotFoundException.class, () -> underTest.getUserDtoById(validCompanyId, invalidCompanyId));
		});
		verify(userMapper, times(1)).toDto(userEntity1);
		verify(userRepository, times(1)).findByIdAndCompanyId(validUserId, validCompanyId);
	}
	
    /**
     * Tests creating and retrieving one user.
     * Expects that one user is successfully saved, mapped and retrieved.
     */
	@Test
	public void createUser_CreateUser_ReturnOneUserDto() {
		// Arrange
		UserRequestDto userRequestDto1 = new UserRequestDto(notExistingUsername);
		UserRequestDto userRequestDto2 = new UserRequestDto(alreadyExistingUsername);
		
		// Mock
		when(companyService.getEntityById(validCompanyId))
		   				   .thenReturn(Optional.of(companyEntity));
		when(companyService.getEntityById(invalidCompanyId))
		   				   .thenReturn(Optional.empty());
		when(userRepository.findByUsername(notExistingUsername))
						   .thenReturn(Optional.empty());
		when(userRepository.findByUsername(alreadyExistingUsername))
		   				   .thenReturn(Optional.of(userMock));
		when(userRepository.save(Mockito.any(UserEntity.class)))
						   .thenReturn(userEntity1);
		when(roleService.getEntityByRole(Role.Developer))
						   .thenReturn(Optional.of(developerRoleEntity));
		
		// Act
		UserResponseDto userResponseDto = underTest.createUser(userRequestDto1, validCompanyId);
		
		// Assert
		assertAll(() -> {
			assertNotNull(userResponseDto);
			assertEquals(userEntity1.getId(), userResponseDto.id());
			assertEquals(userEntity1.getUsername(), userResponseDto.username());
			assertEquals(userEntity1.getCompany().getName(), userResponseDto.company());
			assertEquals(userEntity1.getRole().getRole(), userResponseDto.role());
			assertEquals(userEntity1.getApiKey(), userResponseDto.apiKey());
			assertThrows(EntityNotFoundException.class, () -> underTest.createUser(userRequestDto1, invalidCompanyId));
			assertThrows(EntityAlreadyExistsException.class, () -> underTest.createUser(userRequestDto2, validCompanyId));
		});
		verify(userMapper, times(1)).toEntity(eq(userRequestDto1), Mockito.any(CompanyEntity.class), Mockito.any(RoleEntity.class));
		verify(userRepository, times(1)).save(Mockito.any(UserEntity.class));
		verify(userMapper, times(1)).toDto(userEntity1);
	}
	
    /**
     * Tests updating one user.
     * Expects that one user is successfully updated, mapped and retrieved.
     */
	@Test
	public void updateUser_UpdateUserFields_ReturnOneUserDto() {
		// Arrange
		UserRequestDto userRequestDto1 = new UserRequestDto(notExistingUsername);
		UserRequestDto userRequestDto2 = new UserRequestDto(alreadyExistingUsername);
		UserRequestDto userRequestDto3 = new UserRequestDto(blankUsername);
		
		// Mock
		when(companyService.entityExists(validCompanyId))
		   				   .thenReturn(true);
		when(companyService.entityExists(invalidCompanyId))
		   				   .thenReturn(false);
		when(userRepository.findByIdAndCompanyId(validUserId, validCompanyId))
						   .thenReturn(Optional.of(userEntity1));
		when(userRepository.findByIdAndCompanyId(invalidUserId, validCompanyId))
		   				   .thenReturn(Optional.empty());
		when(securityContext.getAuthentication())
							.thenReturn(authentication);
        when(authentication.getPrincipal())
        				   .thenReturn(requestingAdminEntity);
		when(userRepository.findByUsername(notExistingUsername))
						   .thenReturn(Optional.empty());
		when(userRepository.findByUsername(alreadyExistingUsername))
		   				   .thenReturn(Optional.of(userMock));
		when(userRepository.save(Mockito.any(UserEntity.class)))
			  			   .then(AdditionalAnswers.returnsFirstArg());
		
		// Act
		UserResponseDto userResponseDto = underTest.updateUser(userRequestDto1, validCompanyId, validUserId);
		
		// Assert
		assertAll(() -> {
			assertEquals(userEntity1.getId(), userResponseDto.id());
			assertEquals(userRequestDto1.username(), userResponseDto.username());
			assertEquals(userEntity1.getCompany().getName(), userResponseDto.company());
			assertEquals(userEntity1.getRole().getRole(), userResponseDto.role());
			assertEquals(userEntity1.getApiKey(), userResponseDto.apiKey());
			assertThrows(EntityNotFoundException.class, () -> underTest.updateUser(userRequestDto1, invalidCompanyId, validUserId));
			assertThrows(EntityNotFoundException.class, () -> underTest.updateUser(userRequestDto1, validCompanyId, invalidUserId));
			assertThrows(EntityAlreadyExistsException.class, () -> underTest.updateUser(userRequestDto2, validCompanyId, validUserId));
			assertThrows(ValueNotAllowedException.class, () -> underTest.updateUser(userRequestDto3, validCompanyId, validUserId));
		});
		verify(userMapper, times(1)).updateEntity(userEntity1, userRequestDto1);
		verify(userRepository, times(1)).save(Mockito.any(UserEntity.class));
		verify(userMapper, times(1)).toDto(userEntity1);
	}
	
    /**
     * Tests updating role of one user.
     * Expects that role of user is successfully updated.
     */
	@Test
	public void assignRoleToUser_ChangeRoleOfUser_ReturnOneUserDto() {
		// Arrange
		RoleEntity updatedRoleEntity = TestDataUtil.createProjectManagerRole();
		
		// Mock
		when(companyService.entityExists(validCompanyId))
		   				   .thenReturn(true);
		when(companyService.entityExists(invalidCompanyId))
		   				   .thenReturn(false);
		when(userRepository.findByIdAndCompanyId(validUserId, validCompanyId))
						   .thenReturn(Optional.of(userEntity1));
		when(userRepository.findByIdAndCompanyId(invalidUserId, validCompanyId))
		   				   .thenReturn(Optional.empty());
		when(securityContext.getAuthentication())
							.thenReturn(authentication);
        when(authentication.getPrincipal())
        				   .thenReturn(requestingAdminEntity);
        when(roleService.getEntityById(validRoleId))
        				.thenReturn(Optional.of(updatedRoleEntity));
        when(roleService.getEntityById(invalidRoleId))
						.thenReturn(Optional.empty());
		when(userRepository.save(Mockito.any(UserEntity.class)))
						   .thenReturn(userEntity1);
		
		// Act
		underTest.assignRoleToUser(validCompanyId, validUserId, validRoleId);
		
		// Assert
		assertAll(() -> {
			assertEquals(updatedRoleEntity, userEntity1.getRole());
			assertThrows(EntityNotFoundException.class, () -> underTest.assignRoleToUser(invalidCompanyId, validUserId, validRoleId));
			assertThrows(EntityNotFoundException.class, () -> underTest.assignRoleToUser(validCompanyId, invalidUserId, validRoleId));
			assertThrows(EntityNotFoundException.class, () -> underTest.assignRoleToUser(validCompanyId, validUserId, invalidRoleId));
		});
		verify(userRepository, times(1)).save(Mockito.any(UserEntity.class));
		verify(userMapper, times(1)).toDto(userEntity1);
	}
	
    /**
     * Tests removing one user.
     * Expects that one user is successfully deleted.
     */
	@Test
	public void deleteUser_RemoveUser_ReturnNothing() {
		// Mock
		when(companyService.entityExists(validCompanyId))
		   				   .thenReturn(true);
		when(companyService.entityExists(invalidCompanyId))
		   				   .thenReturn(false);
		when(userRepository.findByIdAndCompanyId(validUserId, validCompanyId))
						   .thenReturn(Optional.of(userEntity1));
		when(userRepository.findByIdAndCompanyId(invalidUserId, validCompanyId))
		   				   .thenReturn(Optional.empty());
		when(securityContext.getAuthentication())
						    .thenReturn(authentication);
		when(authentication.getPrincipal())
						   .thenReturn(requestingAdminEntity);
		
		// Act
		underTest.deleteUser(validCompanyId, validUserId);
		
		// Assert
		assertAll(() -> {
			assertThrows(EntityNotFoundException.class, () -> underTest.deleteUser(invalidCompanyId, validUserId));
			assertThrows(EntityNotFoundException.class, () -> underTest.deleteUser(validCompanyId, invalidUserId));
		});
		verify(userRepository, times(1)).deleteById(validUserId);
	}
}
