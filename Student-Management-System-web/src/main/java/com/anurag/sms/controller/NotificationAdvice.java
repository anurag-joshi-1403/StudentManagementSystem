package com.anurag.sms.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.thymeleaf.context.LazyContextVariable;

import com.anurag.sms.dto.Notification;
import com.anurag.sms.service.NotificationService;

/**
 * Gives every page the navbar's notifications (F2). The panel used to be
 * three hardcoded items with a badge that always said 3.
 *
 * The list is lazy: Thymeleaf loads it the first time a template reads
 * ${notifications}, so redirects, CSV downloads and pages without the
 * navbar never run the queries.
 */
@ControllerAdvice(annotations = Controller.class)
public class NotificationAdvice {

    private final NotificationService notificationService;

    public NotificationAdvice(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    // authentication is null when nobody is signed in (login, register),
    // and the service then returns an empty list
    @ModelAttribute("notifications")
    public LazyContextVariable<List<Notification>> notifications(Authentication authentication) {

        return new LazyContextVariable<>() {
            @Override
            protected List<Notification> loadValue() {
                return notificationService.getNotifications(authentication);
            }
        };
    }
}
