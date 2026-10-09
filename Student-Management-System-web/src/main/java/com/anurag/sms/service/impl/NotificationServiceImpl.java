package com.anurag.sms.service.impl;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import com.anurag.sms.dto.AttendanceSummary;
import com.anurag.sms.dto.Notification;
import com.anurag.sms.entity.Exam;
import com.anurag.sms.entity.Fee;
import com.anurag.sms.repository.AttendanceRepository;
import com.anurag.sms.repository.ExamRepository;
import com.anurag.sms.repository.FeeRepository;
import com.anurag.sms.service.NotificationService;

@Service
public class NotificationServiceImpl implements NotificationService {

    private static final DateTimeFormatter EXAM_DAY = DateTimeFormatter.ofPattern("EEE, d MMM", Locale.ENGLISH);

    // How many names the attendance item lists before "and N more"
    private static final int NAMES_SHOWN = 3;

    private final FeeRepository feeRepository;
    private final ExamRepository examRepository;
    private final AttendanceRepository attendanceRepository;

    public NotificationServiceImpl(FeeRepository feeRepository,
                                   ExamRepository examRepository,
                                   AttendanceRepository attendanceRepository) {
        this.feeRepository = feeRepository;
        this.examRepository = examRepository;
        this.attendanceRepository = attendanceRepository;
    }

    @Override
    public List<Notification> getNotifications(Authentication user) {

        List<Notification> items = new ArrayList<>();

        if (user == null || !user.isAuthenticated() || user instanceof AnonymousAuthenticationToken) {
            return items;
        }

        boolean admin = hasRole(user, "ROLE_ADMIN");
        boolean staff = admin || hasRole(user, "ROLE_TEACHER");
        LocalDate today = LocalDate.now();

        if (admin) {
            overdueFees(today, items);
        }
        if (staff) {
            lowAttendance(items);
        }
        upcomingExams(today, items);

        return items;
    }

    // One item for all overdue fees; it opens the receipt when there is
    // only one, otherwise the fee list, where each one has its badge
    private void overdueFees(LocalDate today, List<Notification> items) {

        List<Fee> overdue = feeRepository.findByPaymentStatusNotAndDueDateBefore("Paid", today);
        if (overdue.isEmpty()) {
            return;
        }

        BigDecimal total = overdue.stream()
                .map(Fee::getAmount)
                .filter(a -> a != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        String detail = rupees(total) + " unpaid past the due date";
        if (overdue.size() == 1) {
            Fee fee = overdue.get(0);
            detail = fee.getStudent().getFirstName() + " " + fee.getStudent().getLastName()
                    + " · " + detail;
        }

        items.add(new Notification("bi-cash-stack", "red",
                overdue.size() + (overdue.size() == 1 ? " overdue fee" : " overdue fees"),
                detail,
                overdue.size() == 1 ? "/fee/view/" + overdue.get(0).getId() : "/fee"));
    }

    // One item naming the students under 75%, linking to the report
    private void lowAttendance(List<Notification> items) {

        List<AttendanceSummary> low = attendanceRepository.findBelowMinimum(AttendanceSummary.MINIMUM_PERCENT);
        if (low.isEmpty()) {
            return;
        }

        List<String> names = low.stream().limit(NAMES_SHOWN).map(AttendanceSummary::studentName).toList();
        String detail = String.join(", ", names)
                + (low.size() > NAMES_SHOWN ? " and " + (low.size() - NAMES_SHOWN) + " more" : "");

        items.add(new Notification("bi-calendar-x", "orange",
                low.size() + (low.size() == 1 ? " student" : " students") + " below "
                        + (int) AttendanceSummary.MINIMUM_PERCENT + "% attendance",
                detail,
                "/attendance/report"));
    }

    // One item per exam in the next week; a week holds few enough
    private void upcomingExams(LocalDate today, List<Notification> items) {

        for (Exam exam : examRepository.findByExamDateBetweenOrderByExamDateAsc(
                today, today.plusDays(EXAM_WINDOW_DAYS))) {

            long days = ChronoUnit.DAYS.between(today, exam.getExamDate());
            String when = days == 0 ? "today" : days == 1 ? "tomorrow" : "in " + days + " days";

            items.add(new Notification("bi-pencil-square", "blue",
                    exam.getExamName() + " · " + exam.getSubject().getSubjectName(),
                    exam.getExamDate().format(EXAM_DAY) + " (" + when + ")",
                    "/exam/view/" + exam.getId()));
        }
    }

    private static boolean hasRole(Authentication user, String role) {
        for (GrantedAuthority authority : user.getAuthorities()) {
            if (role.equals(authority.getAuthority())) {
                return true;
            }
        }
        return false;
    }

    // ₹60,000 or ₹1,250.50: paise only when there are any
    private static String rupees(BigDecimal amount) {
        NumberFormat format = NumberFormat.getNumberInstance(Locale.ENGLISH);
        format.setMinimumFractionDigits(amount.stripTrailingZeros().scale() > 0 ? 2 : 0);
        format.setMaximumFractionDigits(2);
        return "₹" + format.format(amount);
    }
}
