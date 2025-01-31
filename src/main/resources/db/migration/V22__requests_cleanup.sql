CREATE OR REPLACE PROCEDURE soli_bookings_cleanup_outdated_requests(p_date TIMESTAMP)
    LANGUAGE plpgsql
AS $$
DECLARE
    booking_ids_to_delete INTEGER[];
BEGIN
    SELECT array_agg(id)
    INTO booking_ids_to_delete
    FROM soli_bookings
    WHERE start_date < p_date
      AND EXISTS (
        SELECT 1
        FROM soli_open_requests
        WHERE booking_id = soli_bookings.id
    );

    IF array_length(booking_ids_to_delete, 1) > 0 THEN
        DELETE FROM soli_open_requests
        WHERE booking_id = ANY(booking_ids_to_delete);

        DELETE FROM soli_bookings
        WHERE id = ANY(booking_ids_to_delete);
    END IF;
END;
$$;