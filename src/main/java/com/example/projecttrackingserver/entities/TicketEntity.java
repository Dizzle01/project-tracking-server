package com.example.projecttrackingserver.entities;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity class representing a ticket in the database.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="tickets", uniqueConstraints = {
		@UniqueConstraint(columnNames = {"name", "project_id"})
})
public class TicketEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tickets_id_seq")
	@SequenceGenerator(name = "tickets_id_seq", sequenceName = "tickets_id_seq", allocationSize = 1)
	private long id;
	
	@Column(nullable = false)
	private String name;
	
	private String description;
	
	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDate createdAt;
	
	@Column(name = "updated_at")
	private LocalDate updatedAt;
	
	@ManyToOne
	@JoinColumn(name = "project_id", nullable = false)
	private ProjectEntity project;
	
	@OneToOne
	@JoinColumn(name = "creator_id", nullable = false, updatable = false)
	private UserEntity creator;
	
	@OneToOne
	@JoinColumn(name = "ticket_type_id", nullable = false)
	private TicketTypeEntity ticketType;
	
	@OneToOne
	@JoinColumn(name = "ticket_priority_id", nullable = false)
	private TicketPriorityEntity ticketPriority;
	
	@OneToOne
	@JoinColumn(name = "ticket_status_id", nullable = false)
	private TicketStatusEntity ticketStatus;
}
