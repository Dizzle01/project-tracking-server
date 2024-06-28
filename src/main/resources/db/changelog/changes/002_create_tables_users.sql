--changeset henri:1
CREATE TABLE roles (
    id BIGSERIAL PRIMARY KEY,
    role VARCHAR(255) NOT NULL
);
INSERT INTO roles(role) VALUES ('Admin');
INSERT INTO roles(role) VALUES ('ProjectManager');
INSERT INTO roles(role) VALUES ('Developer');
--rollback drop table roles;

--changeset henri:2
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    api_key VARCHAR(64) UNIQUE NOT NULL,
    created_at DATE NOT NULL,
    company_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    FOREIGN KEY (company_id) REFERENCES companies(id),
    FOREIGN KEY (role_id) REFERENCES roles(id)
);
--rollback drop table users;

--changeset henri:3
CREATE FUNCTION generate_api_key() RETURNS VARCHAR(64) AS $api_key$
BEGIN
    RETURN encode(sha256(random()::text::bytea), 'hex');
END;
$api_key$ LANGUAGE plpgsql;
--rollback drop function generate_api_key();