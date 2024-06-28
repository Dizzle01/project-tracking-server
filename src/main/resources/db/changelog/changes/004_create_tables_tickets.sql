--changeset henri:1
CREATE TABLE ticket_priorities (
    id BIGSERIAL PRIMARY KEY,
    ticket_priority VARCHAR(255) NOT NULL
);
INSERT INTO ticket_priorities(ticket_priority) VALUES ('Low');
INSERT INTO ticket_priorities(ticket_priority) VALUES ('Medium');
INSERT INTO ticket_priorities(ticket_priority) VALUES ('High');
INSERT INTO ticket_priorities(ticket_priority) VALUES ('Urgent');

CREATE TABLE ticket_statuses (
    id BIGSERIAL PRIMARY KEY,
    ticket_status VARCHAR(255) NOT NULL
);
INSERT INTO ticket_statuses(ticket_status) VALUES ('New');
INSERT INTO ticket_statuses(ticket_status) VALUES ('Development');
INSERT INTO ticket_statuses(ticket_status) VALUES ('Testing');
INSERT INTO ticket_statuses(ticket_status) VALUES ('Resolved');

CREATE TABLE ticket_types (
    id BIGSERIAL PRIMARY KEY,
    ticket_type VARCHAR(255) NOT NULL
);
INSERT INTO ticket_types(ticket_type) VALUES ('GeneralTask');
INSERT INTO ticket_types(ticket_type) VALUES ('NewFeature');
INSERT INTO ticket_types(ticket_type) VALUES ('ChangeRequest');
INSERT INTO ticket_types(ticket_type) VALUES ('Enhancement');
INSERT INTO ticket_types(ticket_type) VALUES ('Bug');
--rollback drop table ticket_priorities;
--rollback drop table ticket_statuses;
--rollback drop table ticket_types;

--changeset henri:2
CREATE TABLE tickets (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    created_at DATE NOT NULL,
    updated_at DATE,
    project_id BIGINT NOT NULL,
    creator_id BIGINT NOT NULL,
    ticket_type_id BIGINT NOT NULL,
    ticket_priority_id BIGINT NOT NULL,
    ticket_status_id BIGINT NOT NULL,
    UNIQUE (name, project_id),
    FOREIGN KEY (project_id) REFERENCES projects(id),
    FOREIGN KEY (creator_id) REFERENCES users(id),
    FOREIGN KEY (ticket_type_id) REFERENCES ticket_types(id),
    FOREIGN KEY (ticket_priority_id) REFERENCES ticket_priorities(id),
    FOREIGN KEY (ticket_status_id) REFERENCES ticket_statuses(id)
);

REVOKE UPDATE (created_at) ON tickets FROM PUBLIC;
REVOKE UPDATE (creator_id) ON tickets FROM PUBLIC;
--rollback drop table tickets;