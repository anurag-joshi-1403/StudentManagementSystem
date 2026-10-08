package com.anurag.sms.dto;

import java.util.List;

import com.anurag.sms.utility.CsvHelper.RowError;

/**
 * What a student CSV import did (E18): how many rows were saved, and each
 * skipped row with its reason, in row order.
 */
public record ImportReport(int imported, List<RowError> skipped) {

    /** For example "12 imported · 2 skipped". */
    public String summary() {
        return imported + " imported · " + skipped.size() + " skipped";
    }

    /** True when the file itself was unusable (no header, not CSV). */
    public boolean fileRejected() {
        return skipped.stream().anyMatch(e -> e.row() == 0);
    }
}
