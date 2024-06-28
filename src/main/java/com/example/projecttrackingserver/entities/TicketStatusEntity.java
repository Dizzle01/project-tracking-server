package com.example.projecttrackingserver.entities;

import com.example.projecttrackingserver.enums.TicketStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity class representing a ticket status in the database.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="ticket_statuses")
public class TicketStatusEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ticket_statuses_id_seq")
	@SequenceGenerator(name = "ticket_statuses_id_seq", sequenceName = "ticket_statuses_id_seq", allocationSize = 1)
	private long id;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, name = "ticket_status")
	private TicketStatus ticketStatus;
}
