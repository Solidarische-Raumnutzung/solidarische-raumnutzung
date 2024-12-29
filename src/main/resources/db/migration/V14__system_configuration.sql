CREATE TABLE soli_system_configuration
(
    id                  INT PRIMARY KEY CHECK (id = 1),
    guest_login_enabled BOOLEAN NOT NULL
);

INSERT INTO soli_system_configuration (id, guest_login_enabled) VALUES (1, true);