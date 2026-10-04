package com.example.course_assignment_service.service;

import com.example.course_assignment_service.dto.CourseAssignmentPatchRequest;
import com.example.course_assignment_service.dto.CourseAssignmentRequest;
import com.example.course_assignment_service.dto.CourseAssignmentResponse;

import java.util.List;

public interface CourseAssignmentService {

    CourseAssignmentResponse createCourseAssignment(CourseAssignmentRequest request);
    List<CourseAssignmentResponse> getAllCourseAssignment();
    List<CourseAssignmentResponse> getAllCourseAssignmentByCourseTechnologyId(Long courseTechnologyId);
    List<CourseAssignmentResponse> getAllCourseAssignmentByBatchId(Long batchId);
    List<CourseAssignmentResponse> getAllCourseAssignmentByTrainerId(Long trainerId);
    CourseAssignmentResponse updateCourseAssignment(Long id, CourseAssignmentRequest request);
    CourseAssignmentResponse patchCourseAssignment(Long id, CourseAssignmentPatchRequest request);
}
