package com.anurag.sms.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import com.anurag.sms.TestData;
import com.anurag.sms.entity.Course;
import com.anurag.sms.entity.Student;
import com.anurag.sms.entity.User;
import com.anurag.sms.repository.CourseRepository;
import com.anurag.sms.repository.StudentRepository;
import com.anurag.sms.repository.UserRepository;

/**
 * G11: the "own records only" rule, run through real method security the
 * way Stage I will use it. "owner" is linked to Aarav; "unlinked" has no
 * student record.
 */
@SpringBootTest
@Transactional
class AccessRulesTest {

    // Stands in for a Stage I service method such as a student's marksheet
    static class Marksheets {

        @PreAuthorize("hasRole('ADMIN') or @access.isOwnStudent(#studentId)")
        public String marksheetOf(Long studentId) {
            return "marksheet " + studentId;
        }
    }

    @TestConfiguration
    static class Config {

        @Bean
        Marksheets marksheets() {
            return new Marksheets();
        }
    }

    @Autowired private Marksheets marksheets;
    @Autowired private AccessRules access;
    @Autowired private UserRepository users;
    @Autowired private StudentRepository students;
    @Autowired private CourseRepository courses;

    private Long aaravId;
    private Long diyaId;

    @BeforeEach
    void data() {
        Course course = courses.save(TestData.course("BSC-PHY", "B.Sc Physics"));
        Student aarav = students.save(TestData.student("Aarav", "Sharma", course));
        Student diya = students.save(TestData.student("Diya", "Patel", course));
        aaravId = aarav.getId();
        diyaId = diya.getId();

        User owner = account("owner");
        owner.setStudent(aarav);
        users.save(owner);
        users.save(account("unlinked"));
    }

    private static User account(String username) {
        User u = new User();
        u.setFullName(username);
        u.setUsername(username);
        u.setEmail(username + "@example.com");
        u.setPassword("not-a-real-hash");
        u.setRole("ROLE_STUDENT");
        return u;
    }

    @Test
    @WithMockUser(username = "owner", roles = "STUDENT")
    void aStudentReachesTheirOwnRecord() {
        assertThat(access.isOwnStudent(aaravId)).isTrue();
        assertThat(marksheets.marksheetOf(aaravId)).isEqualTo("marksheet " + aaravId);
    }

    @Test
    @WithMockUser(username = "owner", roles = "STUDENT")
    void aStudentCannotReachSomeoneElsesRecord() {
        assertThat(access.isOwnStudent(diyaId)).isFalse();
        assertThatThrownBy(() -> marksheets.marksheetOf(diyaId))
                .isInstanceOf(AccessDeniedException.class);
    }

    @Test
    @WithMockUser(username = "unlinked", roles = "STUDENT")
    void anAccountWithoutALinkOwnsNothing() {
        assertThat(access.isOwnStudent(aaravId)).isFalse();
        assertThat(access.isOwnStudent(null)).isFalse();
        assertThatThrownBy(() -> marksheets.marksheetOf(aaravId))
                .isInstanceOf(AccessDeniedException.class);
    }

    // The admin is not linked to anyone; the role in the expression lets them in
    @Test
    @WithMockUser(username = "admin_g11", roles = "ADMIN")
    void anAdminReachesEveryRecord() {
        assertThat(access.isOwnStudent(aaravId)).isFalse();
        assertThat(marksheets.marksheetOf(aaravId)).isEqualTo("marksheet " + aaravId);
        assertThat(marksheets.marksheetOf(diyaId)).isEqualTo("marksheet " + diyaId);
    }
}
