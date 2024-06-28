package com.example.projecttrackingserver.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.projecttrackingserver.entities.CompanyEntity;

/**
 * Repository interface for performing CRUD operations on CompanyEntity.
 */
@Repository
public interface CompanyRepository extends CrudRepository<CompanyEntity, Long> {

//	@Query("SELECT c FROM CompanyEntity c JOIN FETCH c.projects WHERE c.id = :companyId")
//	public Optional<CompanyEntity> findByIdWithProjects(@Param("companyId") long companyId);
	
    /**
     * Retrieves an optional CompanyEntity by its name.
     *
     * @param name the name of the company to retrieve
     * @return an Optional containing the CompanyEntity if found, otherwise empty
     */
	public Optional<CompanyEntity> findByName(String name);
}