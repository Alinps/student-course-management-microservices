package com.example.student_service.service;

import java.util.List;

import com.example.student_service.dto.CreateStudentInternalRequest;
import com.example.student_service.dto.StudentPatchRequest;
import com.example.student_service.dto.StudentRequest;
import com.example.student_service.dto.StudentResponse;

public interface StudentService {

    StudentResponse createStudent(StudentRequest request);
    List<StudentResponse> getAllStudents();
    StudentResponse getStudentById(Long id);
    StudentResponse updateStudent(Long id, StudentRequest request);
    void deleteStudent(Long id);
    StudentResponse patchStudent(Long id, StudentPatchRequest request);
    List<StudentResponse> getStudentByIds(List<Long> ids);
    StudentResponse createInternalStudent(CreateStudentInternalRequest request);
    StudentResponse getInternalStudentById(Long id);
}
