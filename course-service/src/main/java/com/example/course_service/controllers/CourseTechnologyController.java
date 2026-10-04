package com.example.course_service.controllers;

import com.example.course_service.dto.CourseTechnologyPatchRequest;
import com.example.course_service.dto.CourseTechnologyRequest;
import com.example.course_service.dto.CourseTechnologyResponse;
import com.example.course_service.dto.IdsRequest;
import com.example.course_service.service.CourseTechnologyService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/course")
@RestController
public class CourseTechnologyController {

    private final CourseTechnologyService courseTechnologyService;

    public CourseTechnologyController(CourseTechnologyService courseTechnologyService) {
        this.courseTechnologyService = courseTechnologyService;
    }

    @PostMapping("/{courseId}/technologies")
    public ResponseEntity<CourseTechnologyResponse> createCourseTechnology(
            @PathVariable Long courseId,
            @Valid @RequestBody CourseTechnologyRequest request) {

        CourseTechnologyResponse response = courseTechnologyService.createCourseTechnology(courseId, request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{courseId}/technologies")
    public ResponseEntity<List<CourseTechnologyResponse>> getTechnologiesByCourse(@PathVariable Long courseId) {

        List<CourseTechnologyResponse> responses = courseTechnologyService.getTechnologiesByCourse(courseId);

        return ResponseEntity.ok(responses);
    }


    @GetMapping("/course-technologies/{id}")
    public ResponseEntity<CourseTechnologyResponse> getCourseTechnologyById(@PathVariable Long id) {

        CourseTechnologyResponse response = courseTechnologyService.getCourseTechnologyById(id);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/course-technologies/{id}")
    public ResponseEntity<CourseTechnologyResponse> updateCourseTechnology(
          @PathVariable  Long id,
          @Valid @RequestBody CourseTechnologyRequest request
    ) {

        CourseTechnologyResponse response = courseTechnologyService.updateCourseTechnology(id,request);

        return ResponseEntity.ok(response);
    }


    @PatchMapping("/course-technologies/{id}")
    public ResponseEntity<CourseTechnologyResponse> patchCourseTechnology(
           @PathVariable Long id,
           @Valid @RequestBody CourseTechnologyPatchRequest request
    ) {

        CourseTechnologyResponse response = courseTechnologyService.patchCourseTechnology(id, request);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/course-technologies/{id}")
    public ResponseEntity<Void> deleteCourseTechnology (
            @PathVariable Long id) {
        courseTechnologyService.deleteCourseTechnology(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/internal/by-ids")
    public ResponseEntity<List<CourseTechnologyResponse>> getCourseTechnologiesById(@RequestBody IdsRequest request) {
        List<CourseTechnologyResponse> responses = courseTechnologyService.getCourseTechnologiesById(request.getIds());
        return ResponseEntity.ok(responses);
    }
}
