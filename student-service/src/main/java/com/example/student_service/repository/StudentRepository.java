package com.example.student_service.repository;

import com.example.student_service.enums.StudentStatus;
import org.springframework.data.jpa.repository.JpaRepository;


import com.example.student_service.models.Student;
import java.util.Optional;


public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByEmail(String email); /*Optional<Student> is used because a student may or may not exist in the database. Instead of returning null, Spring Data JPA returns an Optional, which encourages you to handle the "not found" case safely.*/
    Optional<Student> findByPhone(String phone);
    Optional<Student> findByAuthUserId(Long authUserId);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    long countByStatus(StudentStatus status);
}