-- G3 (#35): one attendance record per student, subject and day. Both
-- attendance forms check first; this index stops anything that gets past
-- them, such as two submissions at the same moment.
--
-- Fails if duplicates already exist. Find them with:
--   SELECT student_id, subject_id, attendance_date, COUNT(*) FROM attendance
--   GROUP BY student_id, subject_id, attendance_date HAVING COUNT(*) > 1;

ALTER TABLE attendance
    ADD CONSTRAINT uk_attendance_student_subject_date
        UNIQUE (student_id, subject_id, attendance_date);
