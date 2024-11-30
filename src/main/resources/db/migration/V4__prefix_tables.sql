ALTER TABLE "booking" RENAME TO "soli_bookings";
ALTER SEQUENCE "booking_seq" RENAME TO soli_bookings_seq;

ALTER TABLE "room" RENAME TO "soli_rooms";
ALTER SEQUENCE "room_seq" RENAME TO soli_rooms_seq;