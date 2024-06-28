package com.example.projecttrackingserver.entities;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Entity class representing a company in the database.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="companies")
public class CompanyEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "companies_id_seq")
	@SequenceGenerator(name = "companies_id_seq", sequenceName = "companies_id_seq", allocationSize = 1)
	private long id;
	
	@Column(unique = true, nullable = false)
	private String name;
	
	private String description;
	
	@ToString.Exclude
	@OneToMany(mappedBy = "company")
	private List<ProjectEntity> projects;
}