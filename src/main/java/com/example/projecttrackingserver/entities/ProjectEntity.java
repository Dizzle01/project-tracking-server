package com.example.projecttrackingserver.entities;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Entity class representing a project in the database.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="projects")
public class ProjectEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "projects_id_seq")
	@SequenceGenerator(name = "projects_id_seq", sequenceName = "projects_id_seq", allocationSize = 1)
	private long id;
	
	@Column(unique = true, nullable = false)
	private String name;
	
	private String description;
	
	@Column(name = "start_date")
	private LocalDate startDate;
	
	@Column(name = "end_date")
	private LocalDate endDate;
	
	@ToString.Exclude
	@OneToOne(optional = false)
	@JoinColumn(name = "project_manager_id")
	private UserEntity projectManager;
	
	@ToString.Exclude
	@ManyToMany
	@JoinTable(
			name = "projects_users",
			joinColumns = @JoinColumn(name = "project_id"),
			inverseJoinColumns = @JoinColumn(name = "user_id")
	)
	private List<UserEntity> members;
	
	@ToString.Exclude
	@ManyToOne
	@JoinColumn(name = "company_id", nullable = false)
	private CompanyEntity company;
	
	@ToString.Exclude
	@OneToMany(mappedBy = "project", orphanRemoval = true)
	private List<TicketEntity> tickets;
	
	/**
	 * Adds a user to the project and updates bidirectional relationships.
	 *
	 * @param userToAdd the user entity to add to the project
	 */
	public void addUserToProject(UserEntity userToAdd) {
		members.add(userToAdd);
		userToAdd.getProjects().add(this);
	}
	
	/**
	 * Removes a user from the project and updates bidirectional relationships.
	 *
	 * @param userToRemove the user entity to remove from the project
	 */
	public void removeUserFromProject(UserEntity userToRemove) {
		members.remove(userToRemove);
		userToRemove.getProjects().remove(this);
	}
}
