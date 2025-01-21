ALTER TABLE soli_rooms ADD COLUMN description TEXT DEFAULT 'Please add a description to this room to help others understand its purpose.' NOT NULL;
ALTER TABLE soli_rooms ADD COLUMN name TEXT DEFAULT 'Unnamed Room' NOT NULL;
UPDATE soli_rooms SET name = 'Room ' || id::text WHERE id >= 0;