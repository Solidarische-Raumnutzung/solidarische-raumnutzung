CREATE SEQUENCE IF NOT EXISTS soli_bookings_staged_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE soli_bookings_staged
(
    id              BIGINT NOT NULL,
    description     VARCHAR(255),
    start_date      TIMESTAMP WITHOUT TIME ZONE,
    end_date        TIMESTAMP WITHOUT TIME ZONE,
    share_room_type SMALLINT,
    room_id         BIGINT,
    user_id         BIGINT,
    priority        SMALLINT,
    CONSTRAINT pk_soli_bookings_staged PRIMARY KEY (id)
);

CREATE TABLE soli_bookings_staged_outstanding_requests
(
    staged_booking_id       BIGINT NOT NULL,
    outstanding_requests_id BIGINT NOT NULL
);

ALTER TABLE soli_bookings_staged
    ADD CONSTRAINT FK_SOLI_BOOKINGS_STAGED_ON_ROOM FOREIGN KEY (room_id) REFERENCES soli_rooms (id);

ALTER TABLE soli_bookings_staged
    ADD CONSTRAINT FK_SOLI_BOOKINGS_STAGED_ON_USER FOREIGN KEY (user_id) REFERENCES soli_users (id);

ALTER TABLE soli_bookings_staged_outstanding_requests
    ADD CONSTRAINT fk_solboostaoutreq_on_staged_booking FOREIGN KEY (staged_booking_id) REFERENCES soli_bookings_staged (id);

ALTER TABLE soli_bookings_staged_outstanding_requests
    ADD CONSTRAINT fk_solboostaoutreq_on_user FOREIGN KEY (outstanding_requests_id) REFERENCES soli_users (id);