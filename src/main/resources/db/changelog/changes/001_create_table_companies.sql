--changeset henri:1
CREATE TABLE companies (
    id BIGSERIAL PRIMARY KEY ,
    name VARCHAR(255) UNIQUE NOT NULL,
    description TEXT
);
--rollback drop table companies;