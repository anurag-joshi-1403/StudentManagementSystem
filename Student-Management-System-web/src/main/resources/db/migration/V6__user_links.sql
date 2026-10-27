-- G10: an account can belong to a student or a teacher, so later stages can
-- show people their own records. Both columns are optional; a unique key
-- lets each student or teacher belong to at most one account (MySQL allows
-- any number of NULLs in a unique key).

ALTER TABLE users
    ADD COLUMN student_id BIGINT NULL,
    ADD COLUMN teacher_id BIGINT NULL,
    ADD CONSTRAINT uk_users_student UNIQUE (student_id),
    ADD CONSTRAINT uk_users_teacher UNIQUE (teacher_id),
    ADD CONSTRAINT fk_users_student FOREIGN KEY (student_id) REFERENCES students (id),
    ADD CONSTRAINT fk_users_teacher FOREIGN KEY (teacher_id) REFERENCES teachers (id);
