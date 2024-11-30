ALTER TABLE "user" RENAME TO "soli_users";

ALTER SEQUENCE "user_seq" RENAME TO soli_users_seq;

INSERT INTO room (id) VALUES (1);