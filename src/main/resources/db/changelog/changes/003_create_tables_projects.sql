--changeset henri:1
CREATE TABLE projects (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL,
    description TEXT,
    start_date DATE,
    end_date DATE,
    project_manager_id BIGINT NOT NULL,
    company_id BIGINT NOT NULL,
    FOREIGN KEY (project_manager_id) REFERENCES users(id),
    FOREIGN KEY (company_id) REFERENCES companies(id)
);
--rollback drop table projects;

--changeset henri:2
CREATE TABLE projects_users (
    project_id BIGINT,
    user_id BIGINT,
    UNIQUE (project_id, user_id),
    FOREIGN KEY (project_id) REFERENCES projects(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);
--rollback drop table projects_users;