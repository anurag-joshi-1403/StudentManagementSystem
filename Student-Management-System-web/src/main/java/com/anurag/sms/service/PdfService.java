package com.anurag.sms.service;

import java.time.LocalDate;

import com.anurag.sms.dto.Marksheet;
import com.anurag.sms.entity.Fee;
import com.anurag.sms.entity.Student;

/**
 * PDF versions of the marksheet (F7) and the fee receipt (F8), laid out
 * like their pages so a printout and a download say the same thing.
 */
public interface PdfService {

    byte[] marksheet(Student student, Marksheet marksheet);

    byte[] feeReceipt(Fee fee, LocalDate issuedOn);
}
