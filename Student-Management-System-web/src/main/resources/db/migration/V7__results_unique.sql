-- #41: one result per student and exam. A second result for the same exam
-- made the marksheet count that exam twice. The result form checks first;
-- this index stops anything that gets past it, such as two submissions at
-- the same moment.
--
-- Fails, and changes nothing, if duplicates already exist. Find them with:
--   SELECT student_id, exam_id, COUNT(*) FROM results
--   GROUP BY student_id, exam_id HAVING COUNT(*) > 1;
-- delete the extra rows (keep the correct mark), then start the app again.

ALTER TABLE results
    ADD CONSTRAINT uk_results_student_exam
        UNIQUE (student_id, exam_id);
