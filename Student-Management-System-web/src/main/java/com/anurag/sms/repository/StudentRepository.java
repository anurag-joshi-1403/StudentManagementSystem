package com.anurag.sms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.anurag.sms.entity.Student;

public interface StudentRepository extends JpaRepository<Student, Long>{

}
