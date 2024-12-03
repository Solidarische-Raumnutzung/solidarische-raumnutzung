CREATE TABLE soli_outstanding_requests
(
    staged_booking_id       BIGINT NOT NULL,
    outstanding_requests_id BIGINT NOT NULL
);

ALTER TABLE soli_outstanding_requests
    ADD CONSTRAINT fk_solboostaoutreq_on_staged_booking FOREIGN KEY (staged_booking_id) REFERENCES soli_bookings (id);

ALTER TABLE soli_outstanding_requests
    ADD CONSTRAINT fk_solboostaoutreq_on_user FOREIGN KEY (outstanding_requests_id) REFERENCES soli_users (id);

CREATE TABLE soli_migration_request_mapping
(
    staged_id BIGINT NOT NULL,
    live_id   BIGINT NOT NULL
);

INSERT INTO soli_migration_request_mapping (staged_id, live_id)
SELECT id, nextval('soli_bookings_seq')
FROM soli_bookings_staged;

INSERT INTO soli_bookings (id, description, start_date, end_date, share_room_type, room_id, user_id, priority)
SELECT mrm.live_id, sbs.description, sbs.start_date, sbs.end_date, sbs.share_room_type, sbs.room_id, sbs.user_id, sbs.priority
FROM soli_bookings_staged sbs
JOIN soli_migration_request_mapping mrm ON sbs.id = mrm.staged_id;

INSERT INTO soli_outstanding_requests (staged_booking_id, outstanding_requests_id)
SELECT mrm.live_id, sora.outstanding_requests_id
FROM soli_bookings_staged_outstanding_requests sora
JOIN soli_migration_request_mapping mrm ON sora.staged_booking_id = mrm.staged_id;

DROP TABLE soli_migration_request_mapping;
DROP TABLE soli_bookings_staged_outstanding_requests;
DROP TABLE soli_bookings_staged;
DROP SEQUENCE soli_bookings_staged_seq;