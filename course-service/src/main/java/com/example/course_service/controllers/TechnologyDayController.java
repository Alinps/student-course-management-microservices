package com.example.course_service.controllers;

import com.example.course_service.dto.TechnologyDayPatchRequest;
import com.example.course_service.dto.TechnologyDayRequest;
import com.example.course_service.dto.TechnologyDayResponse;
import com.example.course_service.service.TechnologyDayService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/course/technologies")
@RestController
public class TechnologyDayController {

    private final TechnologyDayService technologyDayService;

    public TechnologyDayController(TechnologyDayService technologyDayService) {
        this.technologyDayService = technologyDayService;
    }

    @PostMapping("/{technologyId}/days")
    public ResponseEntity<TechnologyDayResponse> createDaySplit(
            @PathVariable Long technologyId,
            @Valid @RequestBody TechnologyDayRequest request)  {
        TechnologyDayResponse response = technologyDayService.createDaySplit(technologyId, request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{technologyId}/days")
    public ResponseEntity<List<TechnologyDayResponse>> getDaySplitByTechnologyId(@PathVariable Long technologyId) {
        List<TechnologyDayResponse> responses = technologyDayService.getDaySplitByTechnologyId(technologyId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{technologyId}/days/all")
    public  ResponseEntity<List<TechnologyDayResponse>>getAllDaySplit() {
        List<TechnologyDayResponse> responses = technologyDayService.getAllDaySplit();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/days/{id}")
    public ResponseEntity<TechnologyDayResponse> getDaySplitById(Long id) {
        TechnologyDayResponse response = technologyDayService.getDaySplitById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{technologyId}/day/{id}")
    public ResponseEntity<TechnologyDayResponse> updateDaySplit(
           @PathVariable Long id,
           @PathVariable Long technologyId,
           @RequestBody TechnologyDayRequest request
    ) {
        TechnologyDayResponse response = technologyDayService.updateDaySplit(id,technologyId, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping ("/{technologyId}/day/{id}")
    public ResponseEntity<TechnologyDayResponse> patchDaySplit(
            @PathVariable Long id,
            @PathVariable Long technologyId,
            @RequestBody TechnologyDayPatchRequest request
    ) {
        TechnologyDayResponse response = technologyDayService.patchDaySplit(id,technologyId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/days/{id}")
    public ResponseEntity<Void> deleteDaySplit(@PathVariable Long id) {

        technologyDayService.deleteDaySplit(id);

        return  ResponseEntity.noContent().build();
    }


}
