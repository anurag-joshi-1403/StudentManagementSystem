package com.anurag.sms.utility;

/**
 * Grade and pass/fail rules for a result, in one place so the result form,
 * the marksheet and the tests agree (#28).
 *
 * Pass/fail comes from the exam's own pass mark; the grade from the
 * percentage. One rule ties them together: a fail is always F, and a pass is
 * never graded below D. Before, 40/100 with a pass mark of 33 was "Pass, F"
 * and 55/100 with a pass mark of 60 was "Fail, D".
 */
public final class GradeCalculator {

    public static final String PASS = "Pass";
    public static final String FAIL = "Fail";

    private GradeCalculator() {
    }

    /** @param total must be at least 1, which Exam's @Min(1) guarantees */
    public static double percentage(int obtained, int total) {
        return (double) obtained / total * 100;
    }

    public static boolean isPass(int obtained, int passingMarks) {
        return obtained >= passingMarks;
    }

    /** The status stored on a result: "Pass" or "Fail". */
    public static String status(boolean pass) {
        return pass ? PASS : FAIL;
    }

    public static String grade(double percentage, boolean pass) {

        if (!pass) {
            return "F";
        }
        if (percentage >= 90) {
            return "A+";
        }
        if (percentage >= 80) {
            return "A";
        }
        if (percentage >= 70) {
            return "B";
        }
        if (percentage >= 60) {
            return "C";
        }
        // 50-59%, and every pass below 50%: the lowest passing grade
        return "D";
    }
}
