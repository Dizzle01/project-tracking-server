package com.example.projecttrackingserver.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.projecttrackingserver.entities.ProjectEntity;

/**
 * Repository interface for performing CRUD operations on ProjectEntity.
 */
@Repository
public interface ProjectRepository extends CrudRepository<ProjectEntity, Long>{

    /**
     * Retrieves an optional ProjectEntity by its name.
     *
     * @param name the name of the project to retrieve
     * @return an Optional containing the ProjectEntity if found, otherwise empty
     */
	public Optional<ProjectEntity> findByName(String name);
	
    /**
     * Retrieves a list of ProjectEntities associated with a specific company ID.
     *
     * @param companyId the ID of the company whose projects are to be retrieved
     * @return a list of ProjectEntity associated with the given company ID
     */
	public List<ProjectEntity> findAllByCompanyId(long companyId);
	
	/**
     * Retrieves an optional ProjectEntity by its ID and company ID.
     *
     * @param projectId the ID of the project to retrieve
     * @param companyId the ID of the company to which the project belongs
     * @return an Optional containing the ProjectEntity if found, otherwise empty
     */
	public Optional<ProjectEntity> findByIdAndCompanyId(long projectId, long companyId);
}
