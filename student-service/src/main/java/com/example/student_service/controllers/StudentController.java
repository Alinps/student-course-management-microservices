package com.example.student_service.controllers;


import com.example.student_service.dto.*;
import org.hibernate.dialect.type.PostgreSQLJsonArrayPGObjectJsonbJdbcTypeConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import com.example.student_service.exception.ServiceUnavailableException;
import com.example.student_service.service.StudentService;


import java.util.ArrayList;
import java.util.List;


import jakarta.validation.Valid;




@RequestMapping("/student")
@RestController
public class StudentController {

    
    private  final RestTemplate restTemplate;
    private final StudentService studentService;

    public StudentController(StudentService studentService, RestTemplate restTemplate) {
        this.studentService = studentService;
        this.restTemplate = restTemplate;
    }


    @PostMapping
    public ResponseEntity<StudentResponse> createStudent(
        @Valid @RequestBody  StudentRequest request) {

            StudentResponse response = studentService.createStudent(request);

            return ResponseEntity.status(HttpStatus.CREATED)
                                    .body(response);

        }


  
    @GetMapping("/{id:\\d+}")
    public ResponseEntity<StudentResponse> getStudentById(
        @PathVariable Long id) {

        StudentResponse response = studentService.getStudentById(id);

        return ResponseEntity.ok(response);

    }

    @GetMapping("/internal/{id:\\d+}")
    public ResponseEntity<StudentResponse> getInternalStudentById(
            @PathVariable Long id) {

        StudentResponse response = studentService.getInternalStudentById(id);

        return ResponseEntity.ok(response);

    }


    @GetMapping("/all")
    public ResponseEntity<List<StudentResponse>> getAllStudents() {

        List<StudentResponse> responses = studentService.getAllStudents();

        return ResponseEntity.ok(responses);
    }



    @PutMapping("/{id}")
    public ResponseEntity<StudentResponse> updateStudent(
       @PathVariable Long id,
       @Valid @RequestBody StudentRequest request) {

        StudentResponse response = studentService.updateStudent(id, request);

        return ResponseEntity.ok(response);
       }

    @PatchMapping("/{id}")
    public ResponseEntity<StudentResponse> patchStudent(
        @PathVariable Long id,
        @Valid @RequestBody StudentPatchRequest request){

        StudentResponse response = studentService.patchStudent(id,request);

        return ResponseEntity.ok(response);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(
        @PathVariable Long id
    ) {
        studentService.deleteStudent(id);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/internal/by-ids")
    public ResponseEntity<List<StudentResponse>> getStudentByIds(@RequestBody IdsRequest request) {
       List<StudentResponse> responses = studentService.getStudentByIds(request.getIds());
       return ResponseEntity.ok(responses);
    }


    @PostMapping("/internal")
    public ResponseEntity<StudentResponse> createInternalStudent(
            @Valid @RequestBody CreateStudentInternalRequest request) {
        StudentResponse response = studentService.createInternalStudent(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    




    @GetMapping("/course-info")
    public String getCourseInfo() {

       try {

            return restTemplate.getForObject(
            "http://course-service/courses",
            String.class);
       } 
       catch (ResourceAccessException ex) {

        throw new ServiceUnavailableException(
            "Course Service is currently unavailable"
        );
        
    }


    }
}
