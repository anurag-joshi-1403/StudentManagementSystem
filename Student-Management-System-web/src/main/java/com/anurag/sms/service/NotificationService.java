package com.anurag.sms.service;

import java.util.List;

import org.springframework.security.core.Authentication;

import com.anurag.sms.dto.Notification;

public interface NotificationService {

    // How far ahead the "exam coming up" notifications look
    int EXAM_WINDOW_DAYS = 7;

    // The notifications this user should see, most urgent first (F1):
    // overdue fees (admins, like the Fees pages), students under 75%
    // attendance (admins and teachers) and exams in the next 7 days
    // (everyone). Empty when nobody is signed in.
    List<Notification> getNotifications(Authentication user);
}
