-- G9: the curriculum, the subjects taught in each course. A subject can
-- belong to several courses, so this is a join table.

CREATE TABLE course_subjects (
    course_id  BIGINT NOT NULL,
    subject_id BIGINT NOT NULL,
    PRIMARY KEY (course_id, subject_id),
    CONSTRAINT fk_course_subjects_course FOREIGN KEY (course_id) REFERENCES courses (id),
    CONSTRAINT fk_course_subjects_subject FOREIGN KEY (subject_id) REFERENCES subjects (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Every course and subject pair an enrollment already uses is part of the
-- curriculum, so existing enrollments stay valid
INSERT INTO course_subjects (course_id, subject_id)
SELECT DISTINCT course_id, subject_id FROM enrollments;
