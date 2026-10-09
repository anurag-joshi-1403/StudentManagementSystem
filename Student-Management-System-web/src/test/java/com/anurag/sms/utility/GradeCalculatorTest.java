package com.anurag.sms.utility;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * F14: the grade rules from D12. A fail is always F; a pass is graded by
 * percentage and is never below D.
 */
class GradeCalculatorTest {

    @Nested
    @DisplayName("grade bands for a pass")
    class Bands {

        @ParameterizedTest(name = "{0}% -> {1}")
        @CsvSource({
                "100,   A+",
                "90,    A+",
                "89.99, A",
                "80,    A",
                "79.99, B",
                "70,    B",
                "69.99, C",
                "60,    C",
                "59.99, D",
                "50,    D",
                "49.99, D",   // below 50% but passed: still the lowest passing grade
                "0,     D",
        })
        void eachEdge(double percentage, String grade) {
            assertThat(GradeCalculator.grade(percentage, true)).isEqualTo(grade);
        }
    }

    @ParameterizedTest(name = "a fail at {0}% is F")
    @CsvSource({ "0", "32.99", "59.99", "95" })
    void failIsAlwaysF(double percentage) {
        assertThat(GradeCalculator.grade(percentage, false)).isEqualTo("F");
    }

    @Nested
    @DisplayName("pass mark")
    class PassMark {

        @Test
        void exactlyThePassMarkPasses() {
            assertThat(GradeCalculator.isPass(33, 33)).isTrue();
        }

        @Test
        void oneBelowFails() {
            assertThat(GradeCalculator.isPass(32, 33)).isFalse();
        }

        @Test
        void aboveThePassMarkPasses() {
            assertThat(GradeCalculator.isPass(100, 33)).isTrue();
        }
    }

    @Test
    void percentageIsObtainedOverTotal() {
        assertThat(GradeCalculator.percentage(400, 1000)).isEqualTo(40.0);
        assertThat(GradeCalculator.percentage(1, 3)).isCloseTo(33.333, org.assertj.core.data.Offset.offset(0.001));
    }

    @Test
    void statusWords() {
        assertThat(GradeCalculator.status(true)).isEqualTo("Pass");
        assertThat(GradeCalculator.status(false)).isEqualTo("Fail");
    }

    @Test
    @DisplayName("the D12 examples: 40/100 with pass mark 33 is D·Pass, 55/100 with pass mark 60 is F·Fail")
    void d12Examples() {
        boolean pass1 = GradeCalculator.isPass(40, 33);
        assertThat(GradeCalculator.status(pass1) + " " + GradeCalculator.grade(GradeCalculator.percentage(40, 100), pass1))
                .isEqualTo("Pass D");

        boolean pass2 = GradeCalculator.isPass(55, 60);
        assertThat(GradeCalculator.status(pass2) + " " + GradeCalculator.grade(GradeCalculator.percentage(55, 100), pass2))
                .isEqualTo("Fail F");
    }
}
