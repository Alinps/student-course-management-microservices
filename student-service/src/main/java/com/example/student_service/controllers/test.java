//package com.example.student_service.controllers;
//
//
//import com.example.student_service.dto.*;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PatchMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import org.springframework.http.ResponseEntity;
//import org.springframework.http.HttpStatus;
//
//import com.example.student_service.service.StudentService;
//
//
//import java.util.List;
//
//
//import jakarta.validation.Valid;
//
//
//
//
//@RequestMapping("/student")
//@RestController
//public class StudentController {
//
//
//
//    private final StudentService studentService;
//
//    public StudentController(StudentService studentService) {
//        this.studentService = studentService;
//    }
//
//
//    @PostMapping
//    public ResponseEntity<StudentResponse> createStudent(
//            @Valid @RequestBody  StudentRequest request) {
//
//        StudentResponse response = studentService.createStudent(request);
//
//        return ResponseEntity.status(HttpStatus.CREATED)
//                .body(response);
//
//    }
//
//
//
//    @GetMapping("/{id:\\d+}")
//    public ResponseEntity<StudentResponse> getStudentById(
//            @PathVariable Long id) {
//
//        StudentResponse response = studentService.getStudentById(id);
//
//        return ResponseEntity.ok(response);
//
//    }
//
//
//    @GetMapping("/all")
//    public ResponseEntity<List<StudentResponse>> getAllStudents() {
//
//        List<StudentResponse> responses = studentService.getAllStudents();
//
//        return ResponseEntity.ok(responses);
//    }
//
//
//
//    @PutMapping("/{id}")
//    public ResponseEntity<StudentResponse> updateStudent(
//            @PathVariable Long id,
//            @Valid @RequestBody StudentRequest request) {
//
//        StudentResponse response = studentService.updateStudent(id, request);
//
//        return ResponseEntity.ok(response);
//    }
//
//    @PatchMapping("/{id}")
//    public ResponseEntity<StudentResponse> patchStudent(
//            @PathVariable Long id,
//            @Valid @RequestBody StudentPatchRequest request){
//
//        StudentResponse response = studentService.patchStudent(id,request);
//
//        return ResponseEntity.ok(response);
//
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteStudent(
//            @PathVariable Long id
//    ) {
//        studentService.deleteStudent(id);
//
//        return ResponseEntity.noContent().build();
//    }
//
//
//}
