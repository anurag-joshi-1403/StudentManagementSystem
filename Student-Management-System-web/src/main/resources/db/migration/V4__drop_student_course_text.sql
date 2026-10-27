-- G8, second step: every student now links to a course through course_id
-- (V3), so the old free-text column goes. Kept as its own migration so V3's
-- copy could be checked before anything was deleted.

ALTER TABLE students DROP COLUMN course;
