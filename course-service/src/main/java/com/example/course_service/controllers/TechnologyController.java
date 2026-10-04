package com.example.course_service.controllers;

import com.example.course_service.dto.TechnologyPatchRequest;
import com.example.course_service.dto.TechnologyRequest;
import com.example.course_service.dto.TechnologyResponse;
import com.example.course_service.models.Technology;
import com.example.course_service.service.TechnologyService;
import com.example.course_service.service.TechnologyServiceImpl;
import jakarta.validation.Valid;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/course/technologies")
@RestController
public class    TechnologyController {

    private final TechnologyService technologyService;

    public TechnologyController(TechnologyService technologyService) {
        this.technologyService = technologyService;
    }

    @PostMapping
    public ResponseEntity<TechnologyResponse> createTechnology(
            @Valid @RequestBody TechnologyRequest request) {
        TechnologyResponse response = technologyService.createTechnology(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TechnologyResponse> getTechnologyById(@PathVariable Long id) {

        TechnologyResponse response = technologyService.getTechnologyById(id);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/internal/{id}")
    public ResponseEntity<TechnologyResponse> getInternalTechnologyById(@PathVariable Long id) {

        TechnologyResponse response = technologyService.getTechnologyById(id);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    public ResponseEntity<List<TechnologyResponse>> getAllTechnologies() {

        List<TechnologyResponse> responses = technologyService.getAllTechnologies();

        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TechnologyResponse> updateTechnology(
           @PathVariable Long id,
            @Valid @RequestBody TechnologyRequest request
    ) {

        TechnologyResponse response = technologyService.updateTechnology(id, request);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TechnologyResponse> patchTechnology(
            @PathVariable   Long id,
            @Valid @RequestBody TechnologyPatchRequest request
    ) {

        TechnologyResponse response = technologyService.patchTechnology(id, request);

        return ResponseEntity.ok(response);
    }
}

