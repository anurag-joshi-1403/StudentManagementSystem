package com.anurag.sms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.anurag.sms.entity.ActivityLog;

public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {

    // Dashboard: the newest entries, newest first (F5). The id breaks ties
    // between entries written in the same instant, such as an import.
    List<ActivityLog> findTop10ByOrderByCreatedAtDescIdDesc();
}
