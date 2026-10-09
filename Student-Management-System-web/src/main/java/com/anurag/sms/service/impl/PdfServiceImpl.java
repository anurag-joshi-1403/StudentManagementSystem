package com.anurag.sms.service.impl;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

import org.openpdf.text.Document;
import org.openpdf.text.Element;
import org.openpdf.text.Font;
import org.openpdf.text.PageSize;
import org.openpdf.text.Paragraph;
import org.openpdf.text.Phrase;
import org.openpdf.text.Rectangle;
import org.openpdf.text.pdf.PdfPCell;
import org.openpdf.text.pdf.PdfPTable;
import org.openpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.anurag.sms.dto.Marksheet;
import com.anurag.sms.entity.Fee;
import com.anurag.sms.entity.Result;
import com.anurag.sms.entity.Student;
import com.anurag.sms.service.PdfService;

/**
 * Builds the PDFs with OpenPDF (F6): a heading block, then tables. Each
 * document is made in memory and returned as bytes for the download.
 *
 * Text uses the PDF standard Helvetica font, which every reader has and
 * which needs no font file. It has no ₹ glyph, so amounts read "Rs.".
 */
@Service
public class PdfServiceImpl implements PdfService {

    private static final Color INK = new Color(0x1F, 0x29, 0x37);
    private static final Color MUTED = new Color(0x6B, 0x72, 0x80);
    private static final Color HEADER_FILL = new Color(0x21, 0x25, 0x29);
    private static final Color TOTAL_FILL = new Color(0xF1, 0xF3, 0xF5);
    private static final Color RULE = new Color(0xD1, 0xD5, 0xDB);
    private static final Color PASS = new Color(0x19, 0x87, 0x54);
    private static final Color FAIL = new Color(0xDC, 0x35, 0x45);

    private static final Font TITLE = new Font(Font.HELVETICA, 18, Font.BOLD, INK);
    private static final Font INSTITUTION = new Font(Font.HELVETICA, 13, Font.BOLD, INK);
    private static final Font BODY = new Font(Font.HELVETICA, 10, Font.NORMAL, INK);
    private static final Font BODY_BOLD = new Font(Font.HELVETICA, 10, Font.BOLD, INK);
    private static final Font HEAD = new Font(Font.HELVETICA, 9, Font.BOLD, Color.WHITE);
    private static final Font SMALL = new Font(Font.HELVETICA, 8, Font.NORMAL, MUTED);
    private static final Font AMOUNT = new Font(Font.HELVETICA, 16, Font.BOLD, INK);

    private final String institutionName;

    public PdfServiceImpl(@Value("${sms.institution-name}") String institutionName) {
        this.institutionName = institutionName;
    }

    // ------------------------------------------------------------------
    // Marksheet (F7): the same rows, totals and rule as student/marksheet.html
    // ------------------------------------------------------------------

    @Override
    public byte[] marksheet(Student student, Marksheet marksheet) {

        return build("Marksheet - " + name(student), document -> {

            heading(document, "Marksheet",
                    marksheet.isEmpty() ? null : marksheet.status(), marksheet.pass());

            document.add(details(new String[][] {
                    { "Student", name(student) },
                    { "Email", student.getEmail() },
                    { "Course", student.getCourse() },
            }));

            PdfPTable table = new PdfPTable(new float[] { 2.2f, 2.2f, 1.6f, 1.1f, 1.1f, 1.4f, 0.9f, 1.0f });
            table.setWidthPercentage(100);
            table.setSpacingBefore(14);
            table.setHeaderRows(1);

            for (String h : List.of("Exam", "Subject", "Date", "Marks", "Out of", "Percentage", "Grade", "Status")) {
                table.addCell(headCell(h, h.equals("Marks") || h.equals("Out of") || h.equals("Percentage")));
            }

            if (marksheet.isEmpty()) {
                PdfPCell none = cell("No results recorded for this student yet.", SMALL, Element.ALIGN_CENTER);
                none.setColspan(8);
                table.addCell(none);
            }

            for (Result r : marksheet.rows()) {
                table.addCell(cell(r.getExam().getExamName(), BODY, Element.ALIGN_LEFT));
                table.addCell(cell(r.getExam().getSubject().getSubjectName(), BODY, Element.ALIGN_LEFT));
                table.addCell(cell(String.valueOf(r.getExam().getExamDate()), BODY, Element.ALIGN_LEFT));
                table.addCell(cell(String.valueOf(r.getObtainedMarks()), BODY, Element.ALIGN_RIGHT));
                table.addCell(cell(String.valueOf(r.getExam().getTotalMarks()), BODY, Element.ALIGN_RIGHT));
                table.addCell(cell(percent(r.getObtainedMarks() * 100.0 / r.getExam().getTotalMarks()),
                        BODY, Element.ALIGN_RIGHT));
                table.addCell(cell(r.getGrade(), BODY_BOLD, Element.ALIGN_CENTER));
                table.addCell(statusCell(r.getResultStatus(), "Pass".equals(r.getResultStatus())));
            }

            if (!marksheet.isEmpty()) {
                PdfPCell label = totalCell("Total", Element.ALIGN_RIGHT);
                label.setColspan(3);
                table.addCell(label);
                table.addCell(totalCell(String.valueOf(marksheet.totalObtained()), Element.ALIGN_RIGHT));
                table.addCell(totalCell(String.valueOf(marksheet.totalPossible()), Element.ALIGN_RIGHT));
                table.addCell(totalCell(percent(marksheet.percentage()), Element.ALIGN_RIGHT));
                table.addCell(totalCell(marksheet.grade(), Element.ALIGN_CENTER));
                PdfPCell status = statusCell(marksheet.status(), marksheet.pass());
                status.setBackgroundColor(TOTAL_FILL);
                table.addCell(status);
            }
            document.add(table);

            if (!marksheet.isEmpty()) {
                note(document, "The overall result is a pass only if every exam is passed. A fail is graded F, "
                        + "and a pass is graded on the overall percentage, never below D.");
            }
        });
    }

    // ------------------------------------------------------------------
    // Fee receipt (F8): the same facts as fee/fee-view.html
    // ------------------------------------------------------------------

    @Override
    public byte[] feeReceipt(Fee fee, LocalDate issuedOn) {

        boolean paid = "Paid".equals(fee.getPaymentStatus());
        String number = receiptNumber(fee);

        return build((paid ? "Fee Receipt " : "Fee Statement ") + number, document -> {

            heading(document, paid ? "Fee Receipt" : "Fee Statement", null, false);

            PdfPTable facts = new PdfPTable(new float[] { 1, 1 });
            facts.setWidthPercentage(100);
            facts.addCell(plain(paid ? "Receipt No.  " + number : "Statement No.  " + number, BODY_BOLD, Element.ALIGN_LEFT));
            facts.addCell(plain("Issued on  " + issuedOn, BODY, Element.ALIGN_RIGHT));
            document.add(facts);

            String dueDate = String.valueOf(fee.getDueDate()) + (fee.isOverdue() ? "   OVERDUE" : "");

            PdfPTable details = details(new String[][] {
                    { "Student", name(fee.getStudent()) + "  (Student ID " + fee.getStudent().getId() + ")" },
                    { "Course", fee.getStudent().getCourse() },
                    { "Fee Type", fee.getFeeType() },
                    { "Due Date", dueDate },
                    { "Payment Date", fee.getPaymentDate() != null ? fee.getPaymentDate().toString() : "Not paid yet" },
                    { "Payment Method", blankToDash(fee.getPaymentMethod()) },
                    { "Status", fee.getPaymentStatus() },
            });
            details.setSpacingBefore(10);
            document.add(details);

            PdfPTable amount = new PdfPTable(new float[] { 1, 1 });
            amount.setWidthPercentage(100);
            amount.setSpacingBefore(14);
            PdfPCell label = plain(paid ? "Amount Paid" : "Amount Due", INSTITUTION, Element.ALIGN_LEFT);
            PdfPCell value = plain(rupees(fee.getAmount()), AMOUNT, Element.ALIGN_RIGHT);
            for (PdfPCell c : List.of(label, value)) {
                c.setBorder(Rectangle.TOP | Rectangle.BOTTOM);
                c.setBorderColor(RULE);
                c.setPaddingTop(10);
                c.setPaddingBottom(12);
                amount.addCell(c);
            }
            document.add(amount);

            note(document, "This is a computer-generated document and needs no signature.");
        });
    }

    // ------------------------------------------------------------------
    // Building blocks: a heading, a two-column details table, data cells
    // ------------------------------------------------------------------

    private interface Content {
        void addTo(Document document) throws Exception;
    }

    private byte[] build(String title, Content content) {

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4, 40, 40, 40, 40);

        try {
            PdfWriter.getInstance(document, out);
            document.addTitle(title);
            document.addAuthor(institutionName);
            document.addCreator("Student Management System");
            document.open();
            content.addTo(document);
        } catch (Exception e) {
            throw new IllegalStateException("Could not build the PDF \"" + title + "\"", e);
        } finally {
            if (document.isOpen()) {
                document.close();
            }
        }
        return out.toByteArray();
    }

    // Title on the left, an optional Pass/Fail on the right, the
    // institution underneath, then a rule
    private void heading(Document document, String title, String status, boolean pass) throws Exception {

        PdfPTable top = new PdfPTable(new float[] { 3, 1 });
        top.setWidthPercentage(100);
        top.addCell(plain(title, TITLE, Element.ALIGN_LEFT));
        top.addCell(status == null
                ? plain("", BODY, Element.ALIGN_RIGHT)
                : plain(status, new Font(Font.HELVETICA, 14, Font.BOLD, pass ? PASS : FAIL), Element.ALIGN_RIGHT));

        PdfPCell institution = plain(institutionName, INSTITUTION, Element.ALIGN_LEFT);
        institution.setColspan(2);
        institution.setPaddingTop(4);
        institution.setPaddingBottom(8);
        institution.setBorder(Rectangle.BOTTOM);
        institution.setBorderColor(INK);
        institution.setBorderWidth(1.2f);
        top.addCell(institution);

        top.setSpacingAfter(12);
        document.add(top);
    }

    private static PdfPTable details(String[][] rows) throws Exception {

        PdfPTable table = new PdfPTable(new float[] { 1, 2.3f });
        table.setWidthPercentage(100);
        for (String[] row : rows) {
            PdfPCell key = cell(row[0], BODY_BOLD, Element.ALIGN_LEFT);
            key.setBackgroundColor(TOTAL_FILL);
            table.addCell(key);
            table.addCell(cell(row[1], BODY, Element.ALIGN_LEFT));
        }
        return table;
    }

    private static void note(Document document, String text) throws Exception {
        Paragraph p = new Paragraph(text, SMALL);
        p.setSpacingBefore(10);
        document.add(p);
    }

    private static PdfPCell cell(String text, Font font, int align) {
        PdfPCell c = new PdfPCell(new Phrase(text == null ? "" : text, font));
        c.setHorizontalAlignment(align);
        c.setVerticalAlignment(Element.ALIGN_MIDDLE);
        c.setPadding(6);
        c.setBorderColor(RULE);
        return c;
    }

    private static PdfPCell plain(String text, Font font, int align) {
        PdfPCell c = cell(text, font, align);
        c.setBorder(Rectangle.NO_BORDER);
        c.setPadding(0);
        return c;
    }

    private static PdfPCell headCell(String text, boolean right) {
        PdfPCell c = cell(text, HEAD, right ? Element.ALIGN_RIGHT : Element.ALIGN_LEFT);
        c.setBackgroundColor(HEADER_FILL);
        c.setBorderColor(HEADER_FILL);
        return c;
    }

    private static PdfPCell totalCell(String text, int align) {
        PdfPCell c = cell(text, BODY_BOLD, align);
        c.setBackgroundColor(TOTAL_FILL);
        return c;
    }

    private static PdfPCell statusCell(String status, boolean pass) {
        return cell(status, new Font(Font.HELVETICA, 10, Font.BOLD, pass ? PASS : FAIL), Element.ALIGN_CENTER);
    }

    // ------------------------------------------------------------------
    // Formatting, matching the pages
    // ------------------------------------------------------------------

    /** FEE-00007, the number the receipt page shows. */
    public static String receiptNumber(Fee fee) {
        return String.format(Locale.ROOT, "FEE-%05d", fee.getId());
    }

    // One decimal, as the pages show it: 78.6%
    private static String percent(double value) {
        DecimalFormat f = new DecimalFormat("0.0", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
        return f.format(value) + "%";
    }

    private static String rupees(BigDecimal amount) {
        if (amount == null) {
            return "-";
        }
        DecimalFormat f = new DecimalFormat("#,##0.00", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
        return "Rs. " + f.format(amount);
    }

    private static String name(Student s) {
        return s.getFirstName() + " " + s.getLastName();
    }

    private static String blankToDash(String text) {
        return text == null || text.isBlank() ? "-" : text;
    }
}
