--changeset henri:1
INSERT INTO companies(name, description) VALUES ('Nintendo', 'We love gaming!');
INSERT INTO companies(name, description) VALUES ('FromSoftware', 'Git Gud!');

INSERT INTO users(username, api_key, created_at, company_id, role_id) VALUES ('Laura', 'd58829a4479bad34b0d52fc78787e756f7d8825f32fa09a7171e5f55714ed174', NOW(), 1, 3);
INSERT INTO users(username, api_key, created_at, company_id, role_id) VALUES ('Andreas', generate_api_key(), NOW(), 1, 3);
INSERT INTO users(username, api_key, created_at, company_id, role_id) VALUES ('Marvin', generate_api_key(), NOW(), 1, 3);
INSERT INTO users(username, api_key, created_at, company_id, role_id) VALUES ('Emma', generate_api_key(), NOW(), 1, 3);
INSERT INTO users(username, api_key, created_at, company_id, role_id) VALUES ('Christoph', '72671f0b9ed9460a75570d7e1a287f6500d5d4ee370c26ab98a46feca5cd56ef', NOW(), 1, 2);
INSERT INTO users(username, api_key, created_at, company_id, role_id) VALUES ('Heidi', generate_api_key(), NOW(), 1, 2);
INSERT INTO users(username, api_key, created_at, company_id, role_id) VALUES ('Jürgen', 'f7061377bc2b309fe15548ba2e33adab38b3663bba785de1f87c36be9a2e93c2', NOW(), 1, 1);

INSERT INTO users(username, api_key, created_at, company_id, role_id) VALUES ('Hugo', generate_api_key(), NOW(), 2, 3);
INSERT INTO users(username, api_key, created_at, company_id, role_id) VALUES ('Kai', generate_api_key(), NOW(), 2, 3);
INSERT INTO users(username, api_key, created_at, company_id, role_id) VALUES ('Isa', generate_api_key(), NOW(), 2, 3);
INSERT INTO users(username, api_key, created_at, company_id, role_id) VALUES ('Johanna', generate_api_key(), NOW(), 2, 3);
INSERT INTO users(username, api_key, created_at, company_id, role_id) VALUES ('Lukas', generate_api_key(), NOW(), 2, 2);
INSERT INTO users(username, api_key, created_at, company_id, role_id) VALUES ('Nina', generate_api_key(), NOW(), 2, 2);
INSERT INTO users(username, api_key, created_at, company_id, role_id) VALUES ('Simon', generate_api_key(), NOW(), 2, 1);

INSERT INTO users(username, api_key, created_at, company_id, role_id) VALUES ('Thorsten', generate_api_key(), NOW(), 1, 3);
INSERT INTO users(username, api_key, created_at, company_id, role_id) VALUES ('Lisa', generate_api_key(), NOW(), 1, 3);

INSERT INTO projects(name, description, start_date, end_date, project_manager_id, company_id) 
VALUES ('Test-Projekt 1', 'Das ist die Testbeschreibung von Projekt 1', NOW(), NOW() + INTERVAL '6 months', 5, 1);
INSERT INTO projects(name, description, start_date, end_date, project_manager_id, company_id) 
VALUES ('Test-Projekt 2', 'Das ist die Testbeschreibung von Projekt 2', NOW(), NOW() + INTERVAL '2 months', 6, 1);

INSERT INTO projects(name, description, start_date, end_date, project_manager_id, company_id) 
VALUES ('Test-Projekt 3', 'Das ist die Testbeschreibung von Projekt 3', NOW(), NOW() + INTERVAL '4 months', 12, 2);
INSERT INTO projects(name, description, start_date, end_date, project_manager_id, company_id) 
VALUES ('Test-Projekt 4', 'Das ist die Testbeschreibung von Projekt 4', NOW(), NOW() + INTERVAL '9 months', 13, 2);

INSERT INTO tickets(name, description, created_at, updated_at, project_id, creator_id, ticket_type_id, ticket_priority_id, ticket_status_id)
VALUES ('Test-Ticket 1', 'Testbeschreibung von Ticket 1', NOW(), NOW(), 1, 5, 1, 2, 1);
INSERT INTO tickets(name, description, created_at, updated_at, project_id, creator_id, ticket_type_id, ticket_priority_id, ticket_status_id)
VALUES ('Test-Ticket 2', 'Testbeschreibung von Ticket 2', NOW(), NOW(), 1, 1, 5, 4, 4);
INSERT INTO tickets(name, description, created_at, updated_at, project_id, creator_id, ticket_type_id, ticket_priority_id, ticket_status_id)
VALUES ('Test-Ticket 3', 'Testbeschreibung von Ticket 3', NOW(), NOW(), 2, 3, 3, 1, 3);
INSERT INTO tickets(name, description, created_at, updated_at, project_id, creator_id, ticket_type_id, ticket_priority_id, ticket_status_id)
VALUES ('Test-Ticket 4', 'Testbeschreibung von Ticket 4', NOW(), NOW(), 2, 6, 4, 1, 3);

INSERT INTO tickets(name, description, created_at, updated_at, project_id, creator_id, ticket_type_id, ticket_priority_id, ticket_status_id)
VALUES ('Test-Ticket 5', 'Testbeschreibung von Ticket 5', NOW(), NOW(), 3, 9, 1, 3, 2);
INSERT INTO tickets(name, description, created_at, updated_at, project_id, creator_id, ticket_type_id, ticket_priority_id, ticket_status_id)
VALUES ('Test-Ticket 6', 'Testbeschreibung von Ticket 6', NOW(), NOW(), 3, 8, 2, 1, 3);
INSERT INTO tickets(name, description, created_at, updated_at, project_id, creator_id, ticket_type_id, ticket_priority_id, ticket_status_id)
VALUES ('Test-Ticket 7', 'Testbeschreibung von Ticket 7', NOW(), NOW(), 4, 11, 4, 2, 2);
INSERT INTO tickets(name, description, created_at, updated_at, project_id, creator_id, ticket_type_id, ticket_priority_id, ticket_status_id)
VALUES ('Test-Ticket 8', 'Testbeschreibung von Ticket 8', NOW(), NOW(), 4, 13, 3, 4, 1);

INSERT INTO projects_users(project_id, user_id) VALUES (1, 1);
INSERT INTO projects_users(project_id, user_id) VALUES (1, 2);
INSERT INTO projects_users(project_id, user_id) VALUES (1, 5);
INSERT INTO projects_users(project_id, user_id) VALUES (2, 3);
INSERT INTO projects_users(project_id, user_id) VALUES (2, 4);
INSERT INTO projects_users(project_id, user_id) VALUES (2, 6);

INSERT INTO projects_users(project_id, user_id) VALUES (3, 8);
INSERT INTO projects_users(project_id, user_id) VALUES (3, 9);
INSERT INTO projects_users(project_id, user_id) VALUES (3, 12);
INSERT INTO projects_users(project_id, user_id) VALUES (4, 10);
INSERT INTO projects_users(project_id, user_id) VALUES (4, 11);
INSERT INTO projects_users(project_id, user_id) VALUES (4, 13);
--rollback delete from companies where name = 'Nintendo';
--rollback delete from companies where name = 'FromSoftware';

--rollback delete from users where username = 'Laura';
--rollback delete from users where username = 'Andreas';
--rollback delete from users where username = 'Marvin';
--rollback delete from users where username = 'Emma';
--rollback delete from users where username = 'Christoph';
--rollback delete from users where username = 'Heidi';
--rollback delete from users where username = 'Jürgen';

--rollback delete from users where username = 'Hugo';
--rollback delete from users where username = 'Kai';
--rollback delete from users where username = 'Isa';
--rollback delete from users where username = 'Johanna';
--rollback delete from users where username = 'Lukas';
--rollback delete from users where username = 'Nina';
--rollback delete from users where username = 'Simon';

--rollback delete from projects where name = 'Test-Projekt 1';
--rollback delete from projects where name = 'Test-Projekt 2';
--rollback delete from projects where name = 'Test-Projekt 3';
--rollback delete from projects where name = 'Test-Projekt 4';

--rollback delete from tickets where name = 'Test-Ticket 1';
--rollback delete from tickets where name = 'Test-Ticket 2';
--rollback delete from tickets where name = 'Test-Ticket 3';
--rollback delete from tickets where name = 'Test-Ticket 4';
--rollback delete from tickets where name = 'Test-Ticket 5';
--rollback delete from tickets where name = 'Test-Ticket 6';
--rollback delete from tickets where name = 'Test-Ticket 7';
--rollback delete from tickets where name = 'Test-Ticket 8';

--rollback delete from projects_users where project_id = 1 and user_id = 1;
--rollback delete from projects_users where project_id = 1 and user_id = 2;
--rollback delete from projects_users where project_id = 1 and user_id = 5;
--rollback delete from projects_users where project_id = 2 and user_id = 3;
--rollback delete from projects_users where project_id = 2 and user_id = 4;
--rollback delete from projects_users where project_id = 2 and user_id = 6;

--rollback delete from projects_users where project_id = 3 and user_id = 8;
--rollback delete from projects_users where project_id = 3 and user_id = 9;
--rollback delete from projects_users where project_id = 3 and user_id = 12;
--rollback delete from projects_users where project_id = 4 and user_id = 10;
--rollback delete from projects_users where project_id = 4 and user_id = 11;
--rollback delete from projects_users where project_id = 4 and user_id = 13;