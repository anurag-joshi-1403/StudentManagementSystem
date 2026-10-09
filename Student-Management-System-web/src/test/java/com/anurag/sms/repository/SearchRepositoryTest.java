package com.anurag.sms.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.anurag.sms.TestData;
import com.anurag.sms.entity.Attendance;
import com.anurag.sms.entity.Exam;
import com.anurag.sms.entity.Student;
import com.anurag.sms.entity.Subject;
import com.anurag.sms.service.impl.ExamServiceImpl;
import com.anurag.sms.utility.Pages;

/**
 * F15: the attendance and exam searches (A8, A9, #26). Keyword and date are
 * each optional, and both must match when both are given. The old derived
 * query ignored the date whenever the keyword was blank.
 */
@DataJpaTest
class SearchRepositoryTest {

    private static final LocalDate OCT_1 = LocalDate.of(2026, 10, 1);
    private static final LocalDate OCT_2 = LocalDate.of(2026, 10, 2);

    @Autowired
    private TestEntityManager em;

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private ResultRepository resultRepository;

    private Subject maths;
    private Subject chemistry;

    @BeforeEach
    void data() {
        maths = em.persist(TestData.subject("MATH01", "Mathematics"));
        chemistry = em.persist(TestData.subject("CHEM01", "Chemistry"));
    }

    @Nested
    class AttendanceSearch {

        @BeforeEach
        void rows() {
            Student aarav = em.persist(TestData.student("Aarav", "Sharma"));
            Student diya = em.persist(TestData.student("Diya", "Patel"));
            em.persist(TestData.attendance(aarav, maths, OCT_1, "Present"));
            em.persist(TestData.attendance(diya, maths, OCT_1, "Absent"));
            em.persist(TestData.attendance(aarav, chemistry, OCT_2, "Late"));
            em.flush();
        }

        private List<String> search(String keyword, LocalDate date) {
            return attendanceRepository.search(keyword, date, Pages.of(1)).getContent().stream()
                    .map(a -> a.getStudent().getFirstName() + "/" + a.getSubject().getSubjectName() + "/" + a.getAttendanceDate())
                    .sorted()
                    .toList();
        }

        @Test
        void neitherFilterListsEverything() {
            assertThat(search(null, null)).hasSize(3);
        }

        @Test
        void keywordOnlyMatchesStudentOrSubjectIgnoringCase() {
            assertThat(search("aarav", null)).containsExactly(
                    "Aarav/Chemistry/2026-10-02", "Aarav/Mathematics/2026-10-01");
            assertThat(search("CHEM", null)).containsExactly("Aarav/Chemistry/2026-10-02");
        }

        @Test
        void dateOnlyMatchesThatDay() {
            assertThat(search(null, OCT_1)).containsExactly(
                    "Aarav/Mathematics/2026-10-01", "Diya/Mathematics/2026-10-01");
        }

        @Test
        void bothMustMatch() {
            assertThat(search("aarav", OCT_1)).containsExactly("Aarav/Mathematics/2026-10-01");
            assertThat(search("diya", OCT_2)).isEmpty();
        }
    }

    @Nested
    class ExamSearch {

        @BeforeEach
        void rows() {
            em.persist(TestData.exam("Mid sem", maths, LocalDate.of(2026, 7, 31)));
            em.persist(TestData.exam("End sem", maths, LocalDate.of(2026, 12, 10)));
            em.persist(TestData.exam("Mid sem", chemistry, LocalDate.of(2026, 7, 29)));
            em.flush();
        }

        private List<String> search(String keyword, LocalDate date) {
            return examRepository.search(keyword, date, Pages.of(1)).getContent().stream()
                    .map(e -> e.getExamName() + "/" + e.getSubject().getSubjectName())
                    .sorted()
                    .toList();
        }

        @Test
        void neitherFilterListsEverything() {
            assertThat(search(null, null)).hasSize(3);
        }

        @Test
        void keywordOnlyMatchesExamOrSubjectIgnoringCase() {
            assertThat(search("mid", null)).containsExactly("Mid sem/Chemistry", "Mid sem/Mathematics");
            assertThat(search("chemistry", null)).containsExactly("Mid sem/Chemistry");
        }

        @Test
        void dateOnlyMatchesThatDay() {
            assertThat(search(null, LocalDate.of(2026, 12, 10))).containsExactly("End sem/Mathematics");
        }

        @Test
        void bothMustMatch() {
            assertThat(search("mid", LocalDate.of(2026, 7, 29))).containsExactly("Mid sem/Chemistry");
            assertThat(search("end", LocalDate.of(2026, 7, 29))).isEmpty();
        }

        // #26: the service turns a blank keyword into "no keyword filter",
        // so a date-only search from the form (keyword="") still filters
        @Test
        void blankKeywordFromTheFormDoesNotDisableTheDateFilter() {
            ExamServiceImpl service = new ExamServiceImpl(examRepository, resultRepository);

            List<Exam> found = service.searchExam("  ", LocalDate.of(2026, 12, 10), 1).getContent();

            assertThat(found).extracting(Exam::getExamName).containsExactly("End sem");
        }
    }

    @Test
    void attendanceSearchPagesAreFiveRowsSortedById() {
        Student s = em.persist(TestData.student("Kabir", "Mehta"));
        for (int day = 1; day <= 7; day++) {
            em.persist(TestData.attendance(s, maths, LocalDate.of(2026, 10, day), "Present"));
        }
        em.flush();

        var page1 = attendanceRepository.search(null, null, Pages.of(1));
        var page2 = attendanceRepository.search(null, null, Pages.of(2));

        assertThat(page1.getContent()).hasSize(Pages.PAGE_SIZE);
        assertThat(page2.getContent()).hasSize(2);
        assertThat(page1.getTotalElements()).isEqualTo(7);
        assertThat(page1.getContent()).extracting(Attendance::getId).isSorted();
    }
}
