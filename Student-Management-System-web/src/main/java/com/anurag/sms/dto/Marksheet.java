package com.anurag.sms.dto;

import java.util.List;

import com.anurag.sms.entity.Result;
import com.anurag.sms.utility.GradeCalculator;

/**
 * One student's results with their totals, for the marksheet page (E8) and
 * later its PDF (F7). Built by ResultService.getMarksheet.
 *
 * The overall result follows the same rule as a single result (D12): the
 * student passes only by passing every exam, a fail is F, and a pass is
 * graded on the overall percentage and never below D.
 */
public record Marksheet(
        List<Result> rows,
        int totalObtained,
        int totalPossible,
        double percentage,
        boolean pass,
        String grade) {

    public static Marksheet of(List<Result> rows) {

        if (rows.isEmpty()) {
            return new Marksheet(List.of(), 0, 0, 0, false, "-");
        }

        int obtained = 0;
        int possible = 0;
        boolean passedAll = true;

        for (Result row : rows) {
            obtained += row.getObtainedMarks();
            possible += row.getExam().getTotalMarks();

            // The stored status, so the overall agrees with the rows above it
            passedAll &= GradeCalculator.PASS.equals(row.getResultStatus());
        }

        double percentage = GradeCalculator.percentage(obtained, possible);

        return new Marksheet(List.copyOf(rows), obtained, possible, percentage,
                passedAll, GradeCalculator.grade(percentage, passedAll));
    }

    /** "Pass" or "Fail", the same words a single result uses. */
    public String status() {
        return GradeCalculator.status(pass);
    }

    public boolean isEmpty() {
        return rows.isEmpty();
    }
}
