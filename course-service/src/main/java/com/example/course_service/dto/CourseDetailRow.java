package com.example.course_service.dto;

import com.example.course_service.enums.CourseStatus;

import java.math.BigDecimal;

public record CourseDetailRow(

        Long courseId,
        String courseCode,
        String courseName,
        String description,
        Integer duration,
        BigDecimal fee,
        String category,
        CourseStatus status,

        Long technologyId,
        String technologyName,
        Integer displayOrder,
        Integer durationDays,
        Boolean optional,

        Long dayId,
        Integer dayNumber,
        String dayTitle,
        String dayDescription,
        Integer estimatedHours


) {
}
