package com.example.student_service.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.student_service.dto.CreateStudentInternalRequest;
import com.example.student_service.enums.StudentStatus;
import com.example.student_service.exception.ResourceAlreadyExistsException;
import com.example.student_service.exception.ResourceNotFoundException;
import com.example.student_service.security.AuthenticatedUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.example.student_service.repository.StudentRepository;
import com.example.student_service.dto.StudentPatchRequest;
import com.example.student_service.dto.StudentRequest;
import com.example.student_service.dto.StudentResponse;

import com.example.student_service.models.Student;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.Timer;


@Service
public class StudentServiceImpl implements StudentService{

    private final StudentRepository studentRepository;
    private final MeterRegistry meterRegistry;
    private final Timer internalStudentCreationTimer;
    private static final Logger log = LoggerFactory.getLogger(StudentServiceImpl.class);

    public StudentServiceImpl(
            StudentRepository studentRepository,
            MeterRegistry meterRegistry
           ) {
        this.studentRepository = studentRepository;
        this.meterRegistry = meterRegistry;
        this.internalStudentCreationTimer = Timer.builder("students.internal.creation.duration")
                        .description("Time taken for internal student creation operation")
                                .register(this.meterRegistry);

        Gauge.builder(
                "students.active",
                studentRepository,
                repository -> repository.countByStatus(StudentStatus.ACTIVE)
        )
                .description("Current number of active students")
                .register(meterRegistry);
    }

    @Override
    public StudentResponse createStudent(StudentRequest request) {

        log.info("Creating student. email={}, phone={}", request.getEmail(), request.getPhone());

        // check if email already exists
        if (studentRepository.existsByEmail(request.getEmail())) {

            log.warn("Student creation failed. Email already exists. email={}", request.getEmail());

            throw new ResourceAlreadyExistsException("Email already exists. ");
        }

        // check if phone already exists
        if (studentRepository.existsByPhone(request.getPhone())) {

            log.warn("Student creation failed. Phone number already exists. phone={}", request.getPhone());

            throw new ResourceAlreadyExistsException("Phone number already exists.");
        }

        Student student = new Student();

        // convert DTO to Entity
        student.setName(request.getName());
        student.setEmail(request.getEmail());
        student.setPhone(request.getPhone());

        // save to database
        Student savedStudent = studentRepository.save(student);

        // convert Entity to DTO
        StudentResponse response = new StudentResponse();

        response.setId(savedStudent.getId());
        response.setName(savedStudent.getName());
        response.setEmail(savedStudent.getEmail());
        response.setPhone(savedStudent.getPhone());

        log.info("Student created successfully. studentId={}", savedStudent.getId());

        return response;
        

    }


    @Override
    public StudentResponse getStudentById(Long id) {

        log.debug("Fetching student. studentId={}", id);

        Student student = studentRepository.findById(id)
                .orElseThrow(() -> {

                    log.warn("Failed to get Student. Student not found. studentId={}", id);

                    return new ResourceNotFoundException("Student not found with id:" + id);
                });

        Long authUserId = AuthenticatedUser.getUserId();

        if(!AuthenticatedUser.isAdmin() && !student.getAuthUserId().equals(authUserId)){

            log.warn(
                    "Access denied while fetching student. " +
                            "studentId={}, authUserId={}",
                    id,
                    authUserId
            );

            throw new AccessDeniedException("Access denied. You are not allowed to access this student");
        }

        // convert Entity to DTO
        StudentResponse response = new StudentResponse();

        response.setId(student.getId());
        response.setName(student.getName());
        response.setEmail(student.getEmail());
        response.setPhone(student.getPhone());

        return response;

    }

        @Override
        public StudentResponse getInternalStudentById(Long id) {

            log.debug("Fetching student through internal endpoint. studentId={}", id);

            Student student = studentRepository.findById(id)
                    .orElseThrow(() -> {

                        log.warn("Student not found through internal endpoint. studentId={}", id);

                        return new ResourceNotFoundException("Student not found with id:" + id);
                    });

            // convert Entity to DTO
            StudentResponse response = new StudentResponse();

            response.setId(student.getId());
            response.setName(student.getName());
            response.setEmail(student.getEmail());
            response.setPhone(student.getPhone());

            return response;

        }

    @Override
    public List<StudentResponse> getAllStudents() {

        List<Student> students = studentRepository.findAll();

        List<StudentResponse> responses = new ArrayList<>();

        for (Student student: students) {

            StudentResponse response = new StudentResponse();

            response.setId(student.getId());
            response.setName(student.getName());
            response.setEmail(student.getEmail());
            response.setPhone(student.getPhone());

            responses.add(response);
        }

        return responses;
    }


    @Override
    public StudentResponse updateStudent(Long id, StudentRequest request) {

        log.info("Updating student. studentId={}", id);

        // check if student exists
        Student student = studentRepository.findById(id)
                                            .orElseThrow(() -> {

            log.warn("Student not found for update. studentId={}", id);

            return new ResourceNotFoundException("Student not found with id: " + id);
        });

        Long authUserId = AuthenticatedUser.getUserId();

        if (!AuthenticatedUser.isAdmin() && !student.getAuthUserId().equals(authUserId)) {

            log.warn(
                    "Access denied while updating student. " +
                            "studentId={}, authUserId={}",
                    id,
                    authUserId
            );


            throw new AccessDeniedException("Access denied. You are not allowed to access this student");
        }
        
        // check if email is already used by another student
        studentRepository.findByEmail(request.getEmail())
                .ifPresent(existingStudent -> {

                    if (!existingStudent.getId().equals(id)) {

                        log.warn(
                                "Student update failed. Email already exists. " +
                                        "studentId={}",
                                id
                        );

                        throw new ResourceAlreadyExistsException("Email already exists");
                    }
                });
        
        // check if phone is already used by another student
        studentRepository.findByPhone(request.getPhone())
                .ifPresent(existingStudent -> {

                    if (!existingStudent.getId().equals(id)) {

                        log.warn(
                                "Student update failed. Phone number already exists. " +
                                        "studentId={}",
                                id
                        );

                        throw new ResourceAlreadyExistsException("Phone number already exists");
                    }
                });



        // update student details
        student.setName(request.getName());
        student.setEmail(request.getEmail());
        student.setPhone(request.getPhone());

        // save updated student
        Student updatedStudent = studentRepository.save(student);

        // convert Entity to DTO
        StudentResponse response = new StudentResponse();

        response.setId(updatedStudent.getId());
        response.setName(updatedStudent.getName());
        response.setEmail(updatedStudent.getEmail());
        response.setPhone(updatedStudent.getPhone());

        log.info("Student updated successfully. studentId={}", updatedStudent.getId());

        return response;

    }

    @Override
    public void deleteStudent(Long id) {

        log.info("Deleting student. studentId={}", id);


        // check if student exists
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> {

                    log.warn(
                            "Student not found for deletion. studentId={}",
                            id
                    );

                    return new ResourceNotFoundException("Student not found with id: " + id);
                });

        Long authUserId = AuthenticatedUser.getUserId();

        if (!AuthenticatedUser.isAdmin()) {

            log.warn(
                    "Unauthorized student deletion attempt. " +
                            "studentId={}, authUserId={}",
                    id,
                    authUserId
            );


            throw new AccessDeniedException("Access denied. You are not allowed to delete.");
        }

        student.setStatus(StudentStatus.DELETED);
        studentRepository.save(student);

        log.info("Student marked as deleted successfully. studentId={}", id);

    }



    @Override
    public StudentResponse patchStudent(Long id, StudentPatchRequest request) {


            log.info("Patching student. studentId={}", id);


            // check if student exists
            Student student = studentRepository.findById(id)
                    .orElseThrow(() -> {

                        log.warn("Patching failed. Student not found for patch. studentId={}", id);

                        return new ResourceNotFoundException("Student not found with id: " + id);
                    });

            Long authUserId = AuthenticatedUser.getUserId();
            if (!AuthenticatedUser.isAdmin()
                    && !student.getAuthUserId().equals(authUserId)) {

                log.warn(
                        "Access denied while patching student. " +
                                "studentId={}, authUserId={}",
                        id,
                        authUserId
                );

                throw new AccessDeniedException("Access denied. You are not allowed to access this student");
            }

            // check if name field is not empty in dto 
            if (request.getName() != null) {
                student.setName(request.getName());
            }

            // check if email field is not empty in dto
            if (request.getEmail() != null) {

                // check if email is already used by another student
                studentRepository.findByEmail(request.getEmail())
                        .ifPresent(existing -> {

                            if (!existing.getId().equals(id)) {

                                log.warn(
                                        "Student patch failed. Email already exists. " +
                                                "studentId={}",
                                        id
                                );

                                throw new ResourceAlreadyExistsException("Email already exists.");
                            }
                        });

                student.setEmail(request.getEmail());
            }


            // check if phone field is not empty in dto
            if (request.getPhone() != null) {

                // check if phone number is not used by another student
                studentRepository.findByPhone((request.getPhone()))
                        .ifPresent(existing -> {

                            if (!existing.getId().equals(id)) {

                                log.warn(
                                        "Student patch failed. Phone number already exists. " +
                                                "studentId={}",
                                        id
                                );

                                throw new ResourceAlreadyExistsException("Phone number already exists.");
                            }
                        });

                student.setPhone(request.getPhone());
            }

            // Save student to database
            Student updatedStudent = studentRepository.save(student);

            // convert Entity to DTO
            StudentResponse response = new StudentResponse();

            response.setId(updatedStudent.getId());
            response.setName(updatedStudent.getName());
            response.setEmail(updatedStudent.getEmail());
            response.setPhone(updatedStudent.getPhone());

            log.info("Student patched successfully. studentId={}", updatedStudent.getId());

            return response;

    }


     public List<StudentResponse> getStudentByIds(List<Long> ids) {

        List<Student> students = studentRepository.findAllById(ids);
        List<StudentResponse> responses = new ArrayList<>();

        for (Student student : students) {

            StudentResponse response = new StudentResponse();

            response.setId(student.getId());
            response.setName(student.getName());
            response.setEmail(student.getEmail());
            response.setPhone(student.getPhone());

            responses.add(response);
        }

        return responses;
    }


    public StudentResponse createInternalStudent(CreateStudentInternalRequest request) {

        return internalStudentCreationTimer.record(() -> {

            log.info(
                    "Creating student through internal service request. authUserId={}",
                    request.getAuthUserId()
            );


            if (studentRepository.existsByEmail(request.getEmail())) {

                log.warn(
                        "Internal student creation failed. Email already exists."
                );

                throw new ResourceAlreadyExistsException("Email already exists. ");
            }


            Optional<Student> existingStudent =
                    studentRepository.findByAuthUserId(
                            request.getAuthUserId()
                    );

            if (existingStudent.isPresent()) {

                Student student = existingStudent.get();

                log.debug(
                        "Student already exists for authUserId. " +
                                "Returning existing student. studentId={}, authUserId={}",
                        student.getId(),
                        request.getAuthUserId()
                );


                StudentResponse response = new StudentResponse();

                response.setId(student.getId());
                response.setName(student.getName());
                response.setEmail(student.getEmail());
                response.setPhone(student.getPhone());

                return response;

            }


            Student student = new Student();

            student.setAuthUserId(request.getAuthUserId());
            student.setName(request.getName());
            student.setEmail(request.getEmail());
            student.setStatus(StudentStatus.ACTIVE);

            Student savedStudent = studentRepository.save(student);

            meterRegistry.counter("students.created").increment();

            StudentResponse response = new StudentResponse();

            response.setId(savedStudent.getId());
            response.setName(savedStudent.getName());
            response.setEmail(savedStudent.getEmail());
            response.setPhone(savedStudent.getPhone());

            log.info(
                    "Student created successfully through internal service request. " +
                            "studentId={}, authUserId={}",
                    savedStudent.getId(),
                    savedStudent.getAuthUserId()
            );

            return response;

        });


    }
    
}
