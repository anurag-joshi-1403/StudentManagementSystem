package com.anurag.sms.service;

import java.util.List;

import com.anurag.sms.entity.ActivityLog;

public interface ActivityLogService {

    // Writes one entry for the signed-in user (F4), e.g.
    // record(ActivityLog.CREATED, "Student", "Rohan Verma")
    void record(String action, String entityType, String description);

    // Dashboard: the last 10 entries, newest first (F5)
    List<ActivityLog> getRecentActivity();
}
