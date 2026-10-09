package com.anurag.sms.utility;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.anurag.sms.entity.Student;
import com.anurag.sms.utility.CsvHelper.StudentParseResult;

/**
 * The student CSV format (E16-E19): what the parser accepts, what it
 * reports, and that an export parses back to the same values.
 */
class CsvHelperTest {

    private static final String HEADER = "firstName,lastName,email,phone,gender,course,dateOfBirth,address\r\n";

    private static StudentParseResult parse(String csv) throws IOException {
        return CsvHelper.parseStudents(new ByteArrayInputStream(csv.getBytes(StandardCharsets.UTF_8)));
    }

    @Test
    void readsAQuotedAddressAndIsoDate() throws IOException {
        StudentParseResult r = parse(HEADER
                + "Rohan,Verma,rohan.verma@example.com,9876500011,Male,B.Sc Physics,2005-04-12,\"12 Lake Road, Pune\"\r\n");

        assertThat(r.errors()).isEmpty();
        Student s = r.students().get(0).student();
        assertThat(s.getAddress()).isEqualTo("12 Lake Road, Pune");
        assertThat(s.getDateOfBirth()).isEqualTo(LocalDate.of(2005, 4, 12));
        assertThat(r.students().get(0).row()).isEqualTo(2);
    }

    @Test
    void acceptsAnyColumnOrderAnyHeaderCaseAndExcelsByteOrderMark() throws IOException {
        StudentParseResult r = parse("﻿EMAIL,FirstName,lastname,phone,gender,course,dateOfBirth,address\r\n"
                + "a@example.com,Asha,Roy,9876500001,female,B.Sc Physics,2005-01-01,x\r\n");

        assertThat(r.errors()).isEmpty();
        Student s = r.students().get(0).student();
        assertThat(s.getEmail()).isEqualTo("a@example.com");
        assertThat(s.getFirstName()).isEqualTo("Asha");
        assertThat(s.getGender()).isEqualTo("Female");
    }

    @Test
    void reportsBadRowsAndKeepsTheGoodOnes() throws IOException {
        StudentParseResult r = parse(HEADER
                + "A,B,a@example.com,9876500001,Male,B.Sc Physics,01/03/2005,x\r\n"
                + "C,D,c@example.com,9876500002,Robot,B.Sc Physics,2005-01-01,x\r\n"
                + "E,F,e@example.com\r\n"
                + "G,H,g@example.com,9876500004,Other,B.Sc Physics,2005-01-01,x\r\n");

        assertThat(r.students()).hasSize(1);
        assertThat(r.errors()).extracting(Object::toString).containsExactly(
                "row 2: dateOfBirth \"01/03/2005\" is not a date in yyyy-MM-dd form, e.g. 2005-04-12.",
                "row 3: gender \"Robot\" must be Male, Female or Other.",
                "row 4: has 3 columns, expected 8.");
    }

    @Test
    void rejectsAFileWithoutTheHeader() throws IOException {
        StudentParseResult r = parse("name,email\r\nA,a@example.com\r\n");

        assertThat(r.students()).isEmpty();
        assertThat(r.errors()).singleElement().extracting(CsvHelper.RowError::row).isEqualTo(0);
    }

    @Test
    void exportParsesBackToTheSameValuesIncludingAFormulaLookingName() throws IOException {
        Student s = new Student();
        s.setFirstName("=HYPERLINK(\"x\")");
        s.setLastName("Verma");
        s.setEmail("rohan.verma@example.com");
        s.setPhone("9876500011");
        s.setGender("Male");
        s.setCourse("B.Sc Physics");
        s.setDateOfBirth(LocalDate.of(2005, 4, 12));
        s.setAddress("12 Lake Road, Pune");

        String csv = CsvHelper.studentsToCsv(List.of(s));

        // Excel shows the cell as text instead of running it
        assertThat(csv).contains("\"'=HYPERLINK(\"\"x\"\")\"");

        Student back = parse(csv).students().get(0).student();
        assertThat(back.getFirstName()).isEqualTo("=HYPERLINK(\"x\")");
        assertThat(back.getAddress()).isEqualTo("12 Lake Road, Pune");
        assertThat(back.getDateOfBirth()).isEqualTo(LocalDate.of(2005, 4, 12));
    }
}
