UPDATE "soli_users"
SET "user_id" = 'kit/' || "user_id"
WHERE "user_id" IS NOT NULL;

-- Remove the password column (why was it there in the first place?)
ALTER TABLE "soli_users"
DROP COLUMN "password";