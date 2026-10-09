package com.anurag.sms.service.impl;

import java.util.List;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.anurag.sms.entity.ActivityLog;
import com.anurag.sms.repository.ActivityLogRepository;
import com.anurag.sms.service.ActivityLogService;

@Service
public class ActivityLogServiceImpl implements ActivityLogService {

    // Written when no one is signed in, e.g. by a startup task
    private static final String SYSTEM = "system";

    private final ActivityLogRepository activityLogRepository;

    public ActivityLogServiceImpl(ActivityLogRepository activityLogRepository) {
        this.activityLogRepository = activityLogRepository;
    }

    @Override
    public void record(String action, String entityType, String description) {
        activityLogRepository.save(new ActivityLog(action, entityType, description, currentUsername()));
    }

    @Override
    public List<ActivityLog> getRecentActivity() {
        return activityLogRepository.findTop10ByOrderByCreatedAtDescIdDesc();
    }

    private static String currentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) {
            return SYSTEM;
        }
        return auth.getName();
    }
}
