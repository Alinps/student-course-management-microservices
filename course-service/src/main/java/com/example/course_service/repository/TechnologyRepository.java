package com.example.course_service.repository;

import com.example.course_service.models.Technology;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TechnologyRepository extends JpaRepository<Technology, Long> {
    Optional<Technology> findByTechnologyCode(String technologyCode);
    Optional<Technology> findByTechnologyName(String technologyName);
    boolean existsByTechnologyCode(String technologyCode);
    boolean existsByTechnologyName(String technologyName);
}
