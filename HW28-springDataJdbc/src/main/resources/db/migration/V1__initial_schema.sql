CREATE TABLE client(
    id   BIGSERIAL PRIMARY KEY,
    name VARCHAR(50)
);

CREATE TABLE address(
    id        BIGSERIAL PRIMARY KEY,
    street    VARCHAR(50),
    client_id BIGINT UNIQUE,
    CONSTRAINT address_client_id_fkey FOREIGN KEY(client_id) REFERENCES client(id)
);

CREATE TABLE phone(
    id        BIGSERIAL PRIMARY KEY,
    number    VARCHAR(50),
    client_id BIGINT,
    CONSTRAINT phone_client_id_fkey FOREIGN KEY(client_id) REFERENCES client(id)
);

INSERT INTO client (name) VALUES
('John'),
('Alex'),
('Anna');

INSERT INTO address (street, client_id) VALUES
('Street 1', 1),
('Street 2', 2),
('Street 3', 3);

INSERT INTO phone (number, client_id) VALUES
('+79011234567', 1),
('+79021234567', 1),
('+79031234567', 3);