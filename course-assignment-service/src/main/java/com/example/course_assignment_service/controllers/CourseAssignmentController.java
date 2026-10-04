package com.example.course_assignment_service.controllers;

import com.example.course_assignment_service.dto.CourseAssignmentPatchRequest;
import com.example.course_assignment_service.dto.CourseAssignmentRequest;
import com.example.course_assignment_service.dto.CourseAssignmentResponse;
import com.example.course_assignment_service.service.CourseAssignmentService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RequestMapping("course-assignment")
@RestController
public class CourseAssignmentController {

    private final CourseAssignmentService courseAssignmentService;

    public CourseAssignmentController(CourseAssignmentService courseAssignmentService) {
        this.courseAssignmentService = courseAssignmentService;
    }

    @PostMapping("/create")
    public CourseAssignmentResponse createCourseAssignment(
         @Valid @RequestBody CourseAssignmentRequest request
    ) {
        return courseAssignmentService.createCourseAssignment(request);
    }


    @GetMapping("/all")
    public List<CourseAssignmentResponse> getAllCourseAssignment() {
        return courseAssignmentService.getAllCourseAssignment();
    }

    @GetMapping("/courseTechnology/{courseTechnologyId}")
    public List<CourseAssignmentResponse> getAllCourseAssignmentByCourseTechnology(
            @PathVariable Long courseTechnologyId) {
        return courseAssignmentService.getAllCourseAssignmentByCourseTechnologyId(courseTechnologyId);
    }

    @GetMapping("/batch/{batchId}")
    public List<CourseAssignmentResponse> getAllCourseAssignmentByBatchId(
            @PathVariable Long batchId) {
        return courseAssignmentService.getAllCourseAssignmentByBatchId(batchId);
    }

    @GetMapping("/trainer/{trainerId}")
    public List<CourseAssignmentResponse> getAllCourseAssignmentByTrainerId(
            @PathVariable Long trainerId) {
        return courseAssignmentService.getAllCourseAssignmentByTrainerId(trainerId);
    }

    @PutMapping("/{id}")
    public CourseAssignmentResponse updateCourseAssignment(
            @PathVariable Long id,
            @Valid @RequestBody CourseAssignmentRequest request) {
        return courseAssignmentService.updateCourseAssignment(id,request);
    }

    @PatchMapping("/{id}")
    public CourseAssignmentResponse patchCourseAssignment(
            @PathVariable Long id,
            @Valid @RequestBody CourseAssignmentPatchRequest request
    ) {
        return courseAssignmentService.patchCourseAssignment(id,request);
    }
}
