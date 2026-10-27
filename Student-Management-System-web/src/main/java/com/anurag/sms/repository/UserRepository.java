package com.anurag.sms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.anurag.sms.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Find user by username (used during login)
    Optional<User> findByUsername(String username);

    // Check if username already exists
    boolean existsByUsername(String username);

    // Check if email already exists
    boolean existsByEmail(String email);

    // AdminSeeder: has any account been given this role yet?
    boolean existsByRole(String role);

    // Admin user list (F11): oldest account first
    List<User> findAllByOrderByCreatedAtAscIdAsc();

    // G10: the account a student or teacher is linked to, if any
    Optional<User> findByStudentId(Long studentId);

    Optional<User> findByTeacherId(Long teacherId);

    // G11: the signed-in account's student or teacher id, without loading
    // the account. Empty when the account has no link.
    @Query("SELECT u.student.id FROM User u WHERE u.username = :username")
    Optional<Long> findStudentIdByUsername(@Param("username") String username);

    @Query("SELECT u.teacher.id FROM User u WHERE u.username = :username")
    Optional<Long> findTeacherIdByUsername(@Param("username") String username);

    // Deleting a student or teacher keeps the account and drops the link,
    // which would otherwise block the delete with its foreign key
    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.student = NULL WHERE u.student.id = :studentId")
    void unlinkStudent(@Param("studentId") Long studentId);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.teacher = NULL WHERE u.teacher.id = :teacherId")
    void unlinkTeacher(@Param("teacherId") Long teacherId);
}