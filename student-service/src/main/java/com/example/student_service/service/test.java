//package com.example.student_service.service;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import com.example.student_service.enums.StudentStatus;
//import com.example.student_service.exception.ResourceAlreadyExistsException;
//import com.example.student_service.exception.ResourceNotFoundException;
//
//import org.springframework.stereotype.Service;
//
//import com.example.student_service.repository.StudentRepository;
//
//import com.example.student_service.dto.StudentPatchRequest;
//import com.example.student_service.dto.StudentRequest;
//import com.example.student_service.dto.StudentResponse;
//
//import com.example.student_service.models.Student;
//
//
//
//
//@Service
//public class StudentServiceImpl implements StudentService{
//
//    private final StudentRepository studentRepository;
//
//
//    public StudentServiceImpl(StudentRepository studentRepository) {
//        this.studentRepository = studentRepository;
//    }
//
//    @Override
//    public StudentResponse createStudent(StudentRequest request) {
//
//        // check if email already exists
//        if (studentRepository.existsByEmail(request.getEmail())) {
//            throw new ResourceAlreadyExistsException("Email already exists. ");
//        }
//
//        // check if phone already exists
//        if (studentRepository.existsByPhone(request.getPhone())) {
//            throw new ResourceAlreadyExistsException("Phone number already exists.");
//        }
//
//        Student student = new Student();
//
//        // convert DTO to Entity
//        student.setName(request.getName());
//        student.setEmail(request.getEmail());
//        student.setPhone(request.getPhone());
//
//        // save to database
//        Student savedStudent = studentRepository.save(student);
//
//        // convert Entity to DTO
//        StudentResponse response = new StudentResponse();
//
//        response.setId(savedStudent.getId());
//        response.setName(savedStudent.getName());
//        response.setEmail(savedStudent.getEmail());
//        response.setPhone(savedStudent.getPhone());
//
//        return response;
//    }
//
//
//    @Override
//    public StudentResponse getStudentById(Long id) {
//
//        Student student = studentRepository.findById(id)
//                .orElseThrow(() -> {
//                    return new ResourceNotFoundException("Student not found with id:" + id);
//                });
//
//        // convert Entity to DTO
//        StudentResponse response = new StudentResponse();
//
//        response.setId(student.getId());
//        response.setName(student.getName());
//        response.setEmail(student.getEmail());
//        response.setPhone(student.getPhone());
//
//        return response;
//
//    }
//
//
//    @Override
//    public List<StudentResponse> getAllStudents() {
//
//        List<Student> students = studentRepository.findAll();
//
//        List<StudentResponse> responses = new ArrayList<>();
//
//        for (Student student: students) {
//
//            StudentResponse response = new StudentResponse();
//
//            response.setId(student.getId());
//            response.setName(student.getName());
//            response.setEmail(student.getEmail());
//            response.setPhone(student.getPhone());
//
//            responses.add(response);
//        }
//
//        return responses;
//    }
//
//
//    @Override
//    public StudentResponse updateStudent(Long id, StudentRequest request) {
//
//        // check if student exists
//        Student student = studentRepository.findById(id)
//                .orElseThrow(() -> {
//                    return new ResourceNotFoundException("Student not found with id: " + id);
//                });
//
//        // check if email is already used by another student
//        studentRepository.findByEmail(request.getEmail())
//                .ifPresent(existingStudent -> {
//
//                    if (!existingStudent.getId().equals(id)) {
//
//                        throw new ResourceAlreadyExistsException("Email already exists");
//                    }
//                });
//
//        // check if phone is already used by another student
//        studentRepository.findByPhone(request.getPhone())
//                .ifPresent(existingStudent -> {
//
//                    if (!existingStudent.getId().equals(id)) {
//
//                        throw new ResourceAlreadyExistsException("Phone number already exists");
//                    }
//                });
//
//
//
//        // update student details
//        student.setName(request.getName());
//        student.setEmail(request.getEmail());
//        student.setPhone(request.getPhone());
//
//        // save updated student
//        Student updatedStudent = studentRepository.save(student);
//
//        // convert Entity to DTO
//        StudentResponse response = new StudentResponse();
//
//        response.setId(updatedStudent.getId());
//        response.setName(updatedStudent.getName());
//        response.setEmail(updatedStudent.getEmail());
//        response.setPhone(updatedStudent.getPhone());
//
//        return response;
//
//    }
//
//    @Override
//    public void deleteStudent(Long id) {
//
//        // check if student exists
//        Student student = studentRepository.findById(id)
//                .orElseThrow(() -> {
//                    return new ResourceNotFoundException("Student not found with id: " + id);
//                });
//
//
//        student.setStatus(StudentStatus.DELETED);
//        studentRepository.save(student);
//
//    }
//
//
//
//    @Override
//    public StudentResponse patchStudent(Long id, StudentPatchRequest request) {
//
//        // check if student exists
//        Student student = studentRepository.findById(id)
//                .orElseThrow(() -> {
//
//                    return new ResourceNotFoundException("Student not found with id: " + id);
//                });
//
//
//        // check if name field is not empty in dto
//        if (request.getName() != null) {
//            student.setName(request.getName());
//        }
//
//        // check if email field is not empty in dto
//        if (request.getEmail() != null) {
//
//            // check if email is already used by another student
//            studentRepository.findByEmail(request.getEmail())
//                    .ifPresent(existing -> {
//
//                        if (!existing.getId().equals(id)) {
//
//                            throw new ResourceAlreadyExistsException("Email already exists.");
//                        }
//                    });
//
//            student.setEmail(request.getEmail());
//        }
//
//
//        // check if phone field is not empty in dto
//        if (request.getPhone() != null) {
//
//            // check if phone number is not used by another student
//            studentRepository.findByPhone((request.getPhone()))
//                    .ifPresent(existing -> {
//
//                        if (!existing.getId().equals(id)) {
//
//                            throw new ResourceAlreadyExistsException("Phone number already exists.");
//                        }
//                    });
//
//            student.setPhone(request.getPhone());
//        }
//
//        // Save student to database
//        Student updatedStudent = studentRepository.save(student);
//
//        // convert Entity to DTO
//        StudentResponse response = new StudentResponse();
//
//        response.setId(updatedStudent.getId());
//        response.setName(updatedStudent.getName());
//        response.setEmail(updatedStudent.getEmail());
//        response.setPhone(updatedStudent.getPhone());
//
//        return response;
//
//    }
//
//}
