package com.anurag.sms.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.anurag.sms.entity.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {

    Page<Student> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrCourseCourseNameContainingIgnoreCase(
            String firstName,
            String lastName,
            String email,
            String course,
            Pageable pageable);

    boolean existsByEmail(String email);

    long countByGender(String gender);

    // A course with students cannot be deleted (G8)
    long countByCourseId(Long courseId);

    List<Student> findTop5ByOrderByIdDesc();

    // Users page (G10): students no account is linked to yet, by name
    @Query("""
            SELECT s FROM Student s
            WHERE NOT EXISTS (SELECT u.id FROM User u WHERE u.student = s)
            ORDER BY s.firstName, s.lastName
            """)
    List<Student> findWithoutAccount();
}
