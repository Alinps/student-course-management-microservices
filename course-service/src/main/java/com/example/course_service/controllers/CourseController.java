package com.example.course_service.controllers;

import com.example.course_service.dto.CourseDetailResponse;
import com.example.course_service.dto.CourseRequest;
import com.example.course_service.dto.CourseResponse;
import com.example.course_service.service.CourseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/course")
@RestController
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }


    @PostMapping
    public ResponseEntity<CourseResponse> createCourse(
            @Valid @RequestBody CourseRequest request) {

        CourseResponse response = courseService.createCourse(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);

    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseResponse> getCourseById(
            @PathVariable  Long id) {

        CourseResponse response = courseService.getCourseById(id);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/internal/{id}")
    public ResponseEntity<CourseResponse> getInternalCourseById(
            @PathVariable  Long id) {
        System.out.println("Rached course service");

        CourseResponse response = courseService.getCourseById(id);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    public ResponseEntity<List<CourseResponse>> getAllCourse() {

        List <CourseResponse> responses = courseService.getAllCourse();

        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseResponse> updateStudent(
            @PathVariable Long id,
            @Valid @RequestBody  CourseRequest request) {

        CourseResponse response = courseService.updateCourse(id, request);

        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(
            @PathVariable Long id) {
        courseService.deleteCourse(id);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CourseResponse> patchCourse(
            @PathVariable  Long id,
            @Valid @RequestBody CourseRequest request) {

        CourseResponse response = courseService.patchCourse(id, request);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{courseId}/details")
    public ResponseEntity<CourseDetailResponse> getCourseDetails(@PathVariable Long courseId) {
        CourseDetailResponse response = courseService.getCourseDetails(courseId);
        return ResponseEntity.ok(response);
    }
}
