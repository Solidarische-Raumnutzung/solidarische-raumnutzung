CREATE TABLE room_opening_hours (
                                    id SERIAL PRIMARY KEY,
                                    room_id BIGINT NOT NULL REFERENCES soli_rooms(id),
                                    day_of_week VARCHAR(10) NOT NULL,
                                    start_time TIME NOT NULL,
                                    end_time TIME NOT NULL,
                                    CONSTRAINT valid_time_range CHECK (start_time < end_time)
);