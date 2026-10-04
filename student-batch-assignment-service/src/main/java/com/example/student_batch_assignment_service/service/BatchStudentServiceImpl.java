package com.example.student_batch_assignment_service.service;

import com.example.student_batch_assignment_service.client.BatchServiceClient;
import com.example.student_batch_assignment_service.client.StudentServiceClient;
import com.example.student_batch_assignment_service.dto.BatchResponse;
import com.example.student_batch_assignment_service.dto.BatchStudentRequest;
import com.example.student_batch_assignment_service.dto.BatchStudentResponse;
import com.example.student_batch_assignment_service.dto.StudentResponse;
import com.example.student_batch_assignment_service.exception.ResourceNotFoundException;
import com.example.student_batch_assignment_service.mapper.BatchStudentMapper;
import com.example.student_batch_assignment_service.models.BatchStudent;
import com.example.student_batch_assignment_service.repository.BatchStudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BatchStudentServiceImpl implements BatchStudentService {

    private final BatchStudentRepository batchStudentRepository;
    private final BatchServiceClient batchServiceClient;
    private final StudentServiceClient studentServiceClient;
    private final BatchStudentMapper mapper;
    private static final Logger log = LoggerFactory.getLogger(BatchStudentServiceImpl.class);


    public BatchStudentServiceImpl(
            BatchStudentRepository batchStudentRepository,
            BatchServiceClient batchServiceClient,
            StudentServiceClient studentServiceClient,
            BatchStudentMapper mapper
    ) {
        this.batchStudentRepository = batchStudentRepository;
        this.batchServiceClient = batchServiceClient;
        this.studentServiceClient = studentServiceClient;
        this.mapper = mapper;
    }


    public void enrichStudentBatchResponse(List<BatchStudentResponse> responses) {

        if (responses.isEmpty()) {
            return;
        }

        Set<Long> batchIds = responses.stream()
                .map(response -> response.getBatchId())
                .collect(Collectors.toSet());
        Set<Long> studentIds = responses.stream()
                .map(response -> response.getStudentId())
                .collect(Collectors.toSet());

        List<BatchResponse> batches = batchServiceClient.getBatchesByIds(new ArrayList<>(batchIds));
        List<StudentResponse> students = studentServiceClient.getStudentByIds(new ArrayList<>(studentIds));


        Map<Long, BatchResponse> batchNameMap = new HashMap<>();

        for(BatchResponse batch : batches) {
            batchNameMap.put(
                    batch.getId(),
                    batch
            );
        }

        Map<Long, StudentResponse> studentNameMap = new HashMap<>();
        for(StudentResponse student : students) {
            studentNameMap.put(
                    student.getId(),
                    student
            );
        }


        for (BatchStudentResponse response : responses) {
            BatchResponse batchResponse = batchNameMap.get(response.getBatchId());

            if (batchResponse != null) {
                response.setBatchName(batchResponse.getBatchName());
                response.setBatchCode(batchResponse.getBatchCode());
                response.setBatchStatus(batchResponse.getStatus());
            }

            StudentResponse studentResponse = studentNameMap.get(response.getStudentId());

            if (studentResponse != null) {
                response.setStudentName(studentResponse.getName());
                response.setEmail(studentResponse.getEmail());
                response.setPhone(studentResponse.getPhone());
            }
        }


    }






    @Override
    public BatchStudentResponse createBatchStudent(BatchStudentRequest request) {

        log.info(
                "Creating batch-student assignment. batchId={}, studentId={}",
                request.getBatchId(),
                request.getStudentId()
        );

        BatchResponse batchResponse = batchServiceClient.getBatch(request.getBatchId());
        StudentResponse studentResponse = studentServiceClient.getStudent(request.getStudentId());

        BatchStudent entity = mapper.toEntity(request);

        if(request.getEnrollmentStatus()!=null) {
            entity.setStatus(request.getEnrollmentStatus());
        }

        entity.setBatchId(batchResponse.getId());
        entity.setStudentId(studentResponse.getId());

        BatchStudent batchStudent = batchStudentRepository.save(entity);

        BatchStudentResponse response = mapper.toResponseDTO(batchStudent);

        response.setBatchName(batchResponse.getBatchName());
        response.setBatchCode(batchResponse.getBatchCode());
        response.setBatchStatus(batchResponse.getStatus());
        response.setStudentName(studentResponse.getName());
        response.setEmail(studentResponse.getEmail());
        response.setPhone(studentResponse.getPhone());

        log.info(
                "Batch-student assignment created successfully. " +
                        "batchId={}, studentId={}",
                batchStudent.getBatchId(),
                batchStudent.getStudentId()
        );

        return response;
    }


    public List<BatchStudentResponse> getAllBatchStudents() {

        List<BatchStudent>  batchStudents = batchStudentRepository.findAll();
        List<BatchStudentResponse> responses = mapper.toResponseDTOList(batchStudents);

        enrichStudentBatchResponse(responses);

        return responses;
    }



    public List<BatchStudentResponse> getBatchStudentByBatchId(Long batchId) {

        log.debug("Fetching students assigned to batch. batchId={}", batchId);

        List<BatchStudent> batchStudent = batchStudentRepository.findByBatchId(batchId);

        if(batchStudent.isEmpty()) {

            log.warn("No students found for batch. batchId={}", batchId);

            throw new ResourceNotFoundException("No Student found with batch id" +batchId);
        }

        log.debug(
                "Found {} student-batch assignments for batchId={}",
                batchStudent.size(),
                batchId
        );


        List<BatchStudentResponse> responses = mapper.toResponseDTOList(batchStudent);

        enrichStudentBatchResponse(responses);

        return responses;
    }

    public List<BatchStudentResponse> getBatchStudentByStudentId(Long studentId) {

        log.debug("Fetching batches assigned to student. studentId={}", studentId);

        List<BatchStudent> batchStudent = batchStudentRepository.findByStudentId(studentId);

        if(batchStudent.isEmpty()) {

            log.warn("No batch assignments found for student. studentId={}", studentId);

            throw new ResourceNotFoundException("No Student found with student id" +studentId);
        }

        log.debug(
                "Found {} batch assignments for studentId={}",
                batchStudent.size(),
                studentId
        );

        List<BatchStudentResponse> responses = mapper.toResponseDTOList(batchStudent);

        enrichStudentBatchResponse(responses);

        return responses;
    }
}
