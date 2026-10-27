package com.anurag.sms.security;

import org.springframework.stereotype.Component;

/**
 * "Own records only" checks for method security (G11), named "access" so
 * that a rule reads like a sentence:
 *
 * <pre>
 * &#64;PreAuthorize("hasRole('ADMIN') or &#64;access.isOwnStudent(#studentId)")
 * </pre>
 *
 * Each check answers for the signed-in account only. Role checks stay in the
 * expression, so this class knows nothing about roles.
 */
@Component("access")
public class AccessRules {

    private final CurrentUser currentUser;

    public AccessRules(CurrentUser currentUser) {
        this.currentUser = currentUser;
    }

    // True when the signed-in account is linked to this student (G10)
    public boolean isOwnStudent(Long studentId) {
        return studentId != null
                && currentUser.studentId().map(studentId::equals).orElse(false);
    }

    // True when the signed-in account is linked to this teacher (G10)
    public boolean isOwnTeacher(Long teacherId) {
        return teacherId != null
                && currentUser.teacherId().map(teacherId::equals).orElse(false);
    }
}
