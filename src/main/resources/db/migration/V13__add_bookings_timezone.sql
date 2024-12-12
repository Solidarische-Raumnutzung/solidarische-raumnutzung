ALTER TABLE soli_bookings
    ALTER COLUMN start_date
        TYPE TIMESTAMP WITH TIME ZONE
        USING start_date AT TIME ZONE 'UTC';

ALTER TABLE soli_bookings
    ALTER COLUMN end_date
        TYPE TIMESTAMP WITH TIME ZONE
        USING end_date AT TIME ZONE 'UTC';