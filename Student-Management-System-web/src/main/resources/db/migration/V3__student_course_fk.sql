-- G8: a student's course becomes a link to the courses table. It was free
-- text, which could drift from the course catalogue.

ALTER TABLE students ADD COLUMN course_id BIGINT NULL;

-- Match each student's course text to a course name, ignoring case and
-- spaces at either end
UPDATE students s
JOIN courses c ON LOWER(TRIM(s.course)) = LOWER(TRIM(c.course_name))
SET s.course_id = c.id;

-- Stops here with MySQL error 1138 "Invalid use of NULL value" if any
-- student's text matched no course. List them with
--   SELECT id, first_name, last_name, course FROM students WHERE course_id IS NULL;
-- MySQL cannot roll back these ALTERs, so before starting the app again:
-- drop the course_id column, delete version 3 from flyway_schema_history,
-- then add the missing course or correct the text.
ALTER TABLE students MODIFY course_id BIGINT NOT NULL;

ALTER TABLE students
    ADD CONSTRAINT fk_students_course FOREIGN KEY (course_id) REFERENCES courses (id);

-- The text column is no longer mapped, so it must stop requiring a value
-- before new students are saved. V4 drops it.
ALTER TABLE students MODIFY course VARCHAR(255) NULL;
