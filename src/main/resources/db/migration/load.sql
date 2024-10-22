
CREATE TABLE IF NOT EXISTS "public"."appointment" ("id" SERIAL, "patient_id" "INT", "doctor_id" "INT", "date" TIMESTAMP, "status" "appointment_status");

\set file_path :current_dir'/appointment-bbddfb3df5e4fc5f38ae366d5be9b3ed757540a2095b7ff90ccd5357592f0b11.csv'

TRUNCATE TABLE "public"."appointment" CASCADE;

COPY "public"."appointment"("id","patient_id","doctor_id","date","status")
FROM :'file_path'
WITH (FORMAT csv, DELIMITER ',', NULL 'NULL', QUOTE '"');
CREATE TABLE IF NOT EXISTS "public"."diagnosis" ("id" SERIAL, "patient_id" "INT", "disease" "disease", "status" "diagnosis_status");

\set file_path :current_dir'/diagnosis-441e4cd4d03cbb1ef3b2257ca01ddec5c3bb41f93c9c14742c7ea9737407bec3.csv'

TRUNCATE TABLE "public"."diagnosis" CASCADE;

COPY "public"."diagnosis"("id","patient_id","disease","status")
FROM :'file_path'
WITH (FORMAT csv, DELIMITER ',', NULL 'NULL', QUOTE '"');
CREATE TABLE IF NOT EXISTS "public"."patient" ("id" SERIAL, "first_name" TEXT, "last_name" TEXT, "date_of_birth" DATE, "gender" "gender");

\set file_path :current_dir'/patient-1495f661381b8e3a557859744433ad371e86540fd5a2a993da7ba9644a80759d.csv'

TRUNCATE TABLE "public"."patient" CASCADE;

COPY "public"."patient"("id","first_name","last_name","date_of_birth","gender")
FROM :'file_path'
WITH (FORMAT csv, DELIMITER ',', NULL 'NULL', QUOTE '"');
CREATE TABLE IF NOT EXISTS "public"."users" ("id" SERIAL, "first_name" TEXT, "last_name" TEXT, "username" VARCHAR, "password" VARCHAR, "role" "user_role", "permissions" "permission[]");

\set file_path :current_dir'/users-4b5f19126d0e4e447e223c0b8523ec4d7b0db72306168499b9dbc9aa2184a41a.csv'

TRUNCATE TABLE "public"."users" CASCADE;

COPY "public"."users"("id","first_name","last_name","username","password","role","permissions")
FROM :'file_path'
WITH (FORMAT csv, DELIMITER ',', NULL 'NULL', QUOTE '"');