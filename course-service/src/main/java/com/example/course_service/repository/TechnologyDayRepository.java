package com.example.course_service.repository;

import com.example.course_service.models.Technology;
import com.example.course_service.models.TechnologyDay;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface TechnologyDayRepository extends JpaRepository<TechnologyDay, Long> {
    List<TechnologyDay> findByTechnologyId(Long technologyId);
    boolean existsByTechnologyIdAndDayNumber(Long technologyId,Integer dayNumber);
}
