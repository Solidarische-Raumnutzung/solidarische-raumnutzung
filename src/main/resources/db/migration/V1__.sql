CREATE TABLE booking
(
    id              BIGINT NOT NULL,
    description     VARCHAR(255),
    start_date      TIMESTAMP WITHOUT TIME ZONE,
    end_date        TIMESTAMP WITHOUT TIME ZONE,
    share_room_type SMALLINT,
    room_id         BIGINT,
    user_id         BIGINT,
    priority        SMALLINT,
    CONSTRAINT pk_booking PRIMARY KEY (id)
);

CREATE TABLE room
(
    id BIGINT NOT NULL,
    CONSTRAINT pk_room PRIMARY KEY (id)
);

CREATE TABLE "user"
(
    id       BIGINT NOT NULL,
    dtype    VARCHAR(31),
    username VARCHAR(255),
    email    VARCHAR(255),
    password VARCHAR(255),
    CONSTRAINT pk_user PRIMARY KEY (id)
);

ALTER TABLE booking
    ADD CONSTRAINT FK_BOOKING_ON_ROOM FOREIGN KEY (room_id) REFERENCES room (id);

ALTER TABLE booking
    ADD CONSTRAINT FK_BOOKING_ON_USER FOREIGN KEY (user_id) REFERENCES "user" (id);