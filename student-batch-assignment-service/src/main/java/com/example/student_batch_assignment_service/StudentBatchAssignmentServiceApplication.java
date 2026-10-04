package com.example.student_batch_assignment_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class StudentBatchAssignmentServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudentBatchAssignmentServiceApplication.class, args);
    }

}
