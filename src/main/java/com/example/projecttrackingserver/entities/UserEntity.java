package com.example.projecttrackingserver.entities;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Entity class representing a user in the database.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="users")
public class UserEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_id_seq")
	@SequenceGenerator(name = "users_id_seq", sequenceName = "users_id_seq", allocationSize = 1)
	private long id;
	
	@Column(unique = true, nullable = false)
	private String username;
	
	@Column(name = "api_key", nullable = false, unique = true)
	private String apiKey;
	
	@Column(name = "created_at", updatable = false, nullable = false)
	private LocalDate createdAt;
	
	@ToString.Exclude
	@ManyToOne
	@JoinColumn(name = "company_id", nullable = false)
	private CompanyEntity company;
	
	@ToString.Exclude
	@OneToOne
	@JoinColumn(name = "role_id", nullable = false)
	private RoleEntity role;
	
	@ToString.Exclude
	@ManyToMany(mappedBy = "members")
	private List<ProjectEntity> projects;
}
