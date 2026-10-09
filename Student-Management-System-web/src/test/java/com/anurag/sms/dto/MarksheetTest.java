package com.anurag.sms.dto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.data.Offset.offset;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.anurag.sms.entity.Exam;
import com.anurag.sms.entity.Result;

/**
 * The marksheet totals (E7), checked against the hand calculations made
 * when the page was built.
 */
class MarksheetTest {

    private static Result result(int obtained, int total, String status) {
        Exam exam = new Exam();
        exam.setTotalMarks(total);
        exam.setPassingMarks(total / 4);
        Result r = new Result();
        r.setExam(exam);
        r.setObtainedMarks(obtained);
        r.setResultStatus(status);
        return r;
    }

    @Test
    void twoPassesAreTotalledAndGraded() {
        // 780/1000 + 85/100 = 865/1100 = 78.6% -> B, Pass
        Marksheet m = Marksheet.of(List.of(result(780, 1000, "Pass"), result(85, 100, "Pass")));

        assertThat(m.totalObtained()).isEqualTo(865);
        assertThat(m.totalPossible()).isEqualTo(1100);
        assertThat(m.percentage()).isCloseTo(78.636, offset(0.001));
        assertThat(m.pass()).isTrue();
        assertThat(m.grade()).isEqualTo("B");
        assertThat(m.status()).isEqualTo("Pass");
    }

    @Test
    void oneFailedExamFailsTheWholeMarksheet() {
        // 400/1000 (pass) + 20/100 (fail) = 420/1100 = 38.2% -> F, Fail
        Marksheet m = Marksheet.of(List.of(result(400, 1000, "Pass"), result(20, 100, "Fail")));

        assertThat(m.percentage()).isCloseTo(38.182, offset(0.001));
        assertThat(m.pass()).isFalse();
        assertThat(m.grade()).isEqualTo("F");
        assertThat(m.status()).isEqualTo("Fail");
    }

    @Test
    void noResultsGiveAnEmptyMarksheet() {
        Marksheet m = Marksheet.of(List.of());

        assertThat(m.isEmpty()).isTrue();
        assertThat(m.totalPossible()).isZero();
        assertThat(m.grade()).isEqualTo("-");
    }
}
