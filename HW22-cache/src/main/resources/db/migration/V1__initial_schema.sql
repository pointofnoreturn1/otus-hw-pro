CREATE SEQUENCE client_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE address_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE phone_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE client(
    id   BIGINT PRIMARY KEY,
    name VARCHAR(50)
);

CREATE TABLE address(
    id        BIGINT PRIMARY KEY,
    street    VARCHAR(50),
    client_id BIGINT UNIQUE,
    CONSTRAINT address_client_id_fkey FOREIGN KEY(client_id) REFERENCES client(id)
);

CREATE TABLE phone(
    id        BIGINT PRIMARY KEY,
    number    VARCHAR(50),
    client_id BIGINT,
    CONSTRAINT phone_client_id_fkey FOREIGN KEY(client_id) REFERENCES client(id)
);
