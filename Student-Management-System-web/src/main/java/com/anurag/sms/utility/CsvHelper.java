package com.anurag.sms.utility;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import com.anurag.sms.entity.Fee;
import com.anurag.sms.entity.Student;

/**
 * CSV import and export for students (E17, E19) and export for fees (E20).
 * Renamed from the misspelled CscHelper (#18).
 *
 * Students use one header in both directions, matching the Student form,
 * so an exported file imports straight back (E16):
 *
 *   firstName,lastName,email,phone,gender,course,dateOfBirth,address
 *
 * Dates are yyyy-MM-dd. Columns may come in any order and header names
 * ignore case. This class only reads and writes the format; the import
 * service applies Bean Validation, the duplicate-email check and the
 * course check.
 */
public final class CsvHelper {

    public static final List<String> STUDENT_HEADER = List.of(
            "firstName", "lastName", "email", "phone",
            "gender", "course", "dateOfBirth", "address");

    public static final List<String> FEE_HEADER = List.of(
            "id", "student", "email", "feeType", "amount",
            "dueDate", "paymentDate", "paymentStatus", "paymentMethod", "overdue");

    private static final List<String> GENDERS = List.of("Male", "Female", "Other");

    // Excel writes a byte order mark at the start of a "CSV UTF-8" file
    private static final char BOM = '﻿';

    private static final CSVFormat READ = CSVFormat.DEFAULT.builder()
            .setIgnoreEmptyLines(true)
            .setTrim(true)
            .get();

    private CsvHelper() {
    }

    /** A data row that became a Student. row is the spreadsheet row: the header is row 1. */
    public record ParsedStudent(int row, Student student) {
    }

    /** A row that could not be used, and why. Row 0 means the whole file. */
    public record RowError(int row, String message) {

        @Override
        public String toString() {
            return row == 0 ? message : "row " + row + ": " + message;
        }
    }

    public record StudentParseResult(List<ParsedStudent> students, List<RowError> errors) {
    }

    // ------------------------------------------------------------------
    // Import
    // ------------------------------------------------------------------

    /**
     * Reads every data row. A row that cannot be read (wrong column count,
     * a bad date or gender) goes to errors and the rest carry on. A file
     * without the expected header, or that is not valid CSV, gives one
     * row-0 error.
     */
    public static StudentParseResult parseStudents(InputStream in) throws IOException {

        List<ParsedStudent> students = new ArrayList<>();
        List<RowError> errors = new ArrayList<>();
        int row = 1;

        Reader reader = new InputStreamReader(in, StandardCharsets.UTF_8);

        try (CSVParser parser = CSVParser.parse(reader, READ)) {

            Iterator<CSVRecord> records = parser.iterator();

            if (!records.hasNext()) {
                errors.add(new RowError(0, "The file is empty."));
                return new StudentParseResult(students, errors);
            }

            Map<String, Integer> columns = columnIndexes(records.next());
            List<String> missing = STUDENT_HEADER.stream()
                    .filter(name -> !columns.containsKey(name.toLowerCase(Locale.ROOT)))
                    .toList();

            if (!missing.isEmpty()) {
                errors.add(new RowError(0, "The header row is missing " + String.join(", ", missing)
                        + ". The first line must be: " + String.join(",", STUDENT_HEADER)));
                return new StudentParseResult(students, errors);
            }

            while (records.hasNext()) {
                CSVRecord record = records.next();
                row++;
                try {
                    students.add(new ParsedStudent(row, toStudent(record, columns)));
                } catch (IllegalArgumentException e) {
                    errors.add(new RowError(row, e.getMessage()));
                }
            }

        } catch (UncheckedIOException e) {
            // commons-csv reports a broken file (e.g. an unclosed quote) this way
            errors.add(new RowError(0, "The file is not valid CSV after row " + row + ": "
                    + e.getCause().getMessage()));
        }

        return new StudentParseResult(students, errors);
    }

    private static Map<String, Integer> columnIndexes(CSVRecord header) {

        Map<String, Integer> columns = new HashMap<>();

        for (int i = 0; i < header.size(); i++) {
            String name = header.get(i);
            if (i == 0 && !name.isEmpty() && name.charAt(0) == BOM) {
                name = name.substring(1);
            }
            columns.putIfAbsent(name.trim().toLowerCase(Locale.ROOT), i);
        }
        return columns;
    }

    private static Student toStudent(CSVRecord record, Map<String, Integer> columns) {

        int needed = STUDENT_HEADER.stream()
                .mapToInt(name -> columns.get(name.toLowerCase(Locale.ROOT)))
                .max().orElse(0) + 1;

        if (record.size() < needed) {
            throw new IllegalArgumentException("has " + record.size()
                    + " columns, expected " + needed + ".");
        }

        Student student = new Student();
        student.setFirstName(value(record, columns, "firstName"));
        student.setLastName(value(record, columns, "lastName"));
        student.setEmail(value(record, columns, "email"));
        student.setPhone(value(record, columns, "phone"));
        student.setGender(gender(value(record, columns, "gender")));
        student.setCourse(value(record, columns, "course"));
        student.setDateOfBirth(date(value(record, columns, "dateOfBirth")));
        student.setAddress(value(record, columns, "address"));
        return student;
    }

    private static String value(CSVRecord record, Map<String, Integer> columns, String name) {
        return unprotect(record.get(columns.get(name.toLowerCase(Locale.ROOT))));
    }

    // Blank stays blank, so Bean Validation reports it with the form's message
    private static String gender(String raw) {

        if (raw.isEmpty()) {
            return raw;
        }
        for (String g : GENDERS) {
            if (g.equalsIgnoreCase(raw)) {
                return g;
            }
        }
        throw new IllegalArgumentException("gender \"" + raw + "\" must be Male, Female or Other.");
    }

    private static LocalDate date(String raw) {

        if (raw.isEmpty()) {
            return null;
        }
        try {
            return LocalDate.parse(raw);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("dateOfBirth \"" + raw
                    + "\" is not a date in yyyy-MM-dd form, e.g. 2005-04-12.");
        }
    }

    // ------------------------------------------------------------------
    // Export
    // ------------------------------------------------------------------

    /** Students in the import format, so the file can be imported again. */
    public static String studentsToCsv(List<Student> students) {

        return print(STUDENT_HEADER, printer -> {
            for (Student s : students) {
                printer.printRecord(
                        protect(s.getFirstName()), protect(s.getLastName()),
                        protect(s.getEmail()), protect(s.getPhone()),
                        protect(s.getGender()), protect(s.getCourse()),
                        s.getDateOfBirth() == null ? "" : s.getDateOfBirth().toString(),
                        protect(s.getAddress()));
            }
        });
    }

    /** Fees for a spreadsheet (E20). Amounts are plain numbers, dates yyyy-MM-dd. */
    public static String feesToCsv(List<Fee> fees) {

        return print(FEE_HEADER, printer -> {
            for (Fee f : fees) {
                printer.printRecord(
                        f.getId(),
                        protect(f.getStudent().getFirstName() + " " + f.getStudent().getLastName()),
                        protect(f.getStudent().getEmail()),
                        protect(f.getFeeType()),
                        f.getAmount() == null ? "" : f.getAmount().toPlainString(),
                        f.getDueDate() == null ? "" : f.getDueDate().toString(),
                        f.getPaymentDate() == null ? "" : f.getPaymentDate().toString(),
                        protect(f.getPaymentStatus()),
                        protect(f.getPaymentMethod()),
                        f.isOverdue() ? "Yes" : "No");
            }
        });
    }

    private interface Rows {
        void write(CSVPrinter printer) throws IOException;
    }

    // CSVFormat.DEFAULT ends lines with CRLF and quotes only where needed,
    // which is what Excel expects
    private static String print(List<String> header, Rows rows) {

        StringWriter out = new StringWriter();
        CSVFormat format = CSVFormat.DEFAULT.builder()
                .setHeader(header.toArray(String[]::new))
                .get();

        try (CSVPrinter printer = new CSVPrinter(out, format)) {
            rows.write(printer);
        } catch (IOException e) {
            // A StringWriter does not throw; this keeps the signature simple
            throw new UncheckedIOException(e);
        }
        return out.toString();
    }

    // ------------------------------------------------------------------
    // Formula injection
    // ------------------------------------------------------------------

    // Excel runs a cell that starts with = + - or @ as a formula, so a
    // student named "=HYPERLINK(...)" could plant a link in an admin's
    // spreadsheet. Export prefixes such cells with an apostrophe, which
    // Excel shows as text; import removes it again, so a round trip keeps
    // the original value.
    private static String protect(String value) {

        if (value == null) {
            return "";
        }
        if (!value.isEmpty() && "=+-@\t\r".indexOf(value.charAt(0)) >= 0) {
            return "'" + value;
        }
        return value;
    }

    private static String unprotect(String value) {

        if (value.length() > 1 && value.charAt(0) == '\''
                && "=+-@\t\r".indexOf(value.charAt(1)) >= 0) {
            return value.substring(1);
        }
        return value;
    }
}
