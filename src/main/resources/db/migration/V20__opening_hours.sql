CREATE TABLE soli_room_opening_hours
(
    day_of_week SMALLINT NOT NULL CHECK (day_of_week BETWEEN 0 AND 6),
    end_time    TIME     NOT NULL,
    start_time  TIME     NOT NULL,
    room_id     BIGINT   NOT NULL,
    PRIMARY KEY (day_of_week, room_id)
);

ALTER TABLE IF EXISTS soli_room_opening_hours
    ADD CONSTRAINT FK_OPENING_HOURS_ON_ROOM FOREIGN KEY (room_id) REFERENCES soli_rooms;