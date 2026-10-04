package com.example.course_assignment_service.service;

import com.example.course_assignment_service.client.BatchServiceClient;
import com.example.course_assignment_service.client.CourseTechnologyServiceClient;
import com.example.course_assignment_service.client.TrainerServiceClient;
import com.example.course_assignment_service.dto.*;
import com.example.course_assignment_service.enums.AssignmentStatus;
import com.example.course_assignment_service.enums.EmployeeDepartment;
import com.example.course_assignment_service.exception.InvalidAssignmentException;
import com.example.course_assignment_service.exception.ResourceNotFoundException;
import com.example.course_assignment_service.mapper.CourseAssignmentMapper;
import com.example.course_assignment_service.models.CourseAssignment;
import com.example.course_assignment_service.repository.CourseAssignmentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class CourseAssignmentServiceImpl implements CourseAssignmentService{

    private final BatchServiceClient batchServiceClient;
    private final TrainerServiceClient trainerServiceClient;
    private final CourseTechnologyServiceClient courseTechnologyServiceClient;
    private final CourseAssignmentMapper mapper;
    private final CourseAssignmentRepository courseAssignmentRepository;
    private static final Logger log = LoggerFactory.getLogger(CourseAssignmentServiceImpl.class);


    public CourseAssignmentServiceImpl(
            BatchServiceClient batchServiceClient,
            TrainerServiceClient trainerServiceClient,
            CourseTechnologyServiceClient courseTechnologyServiceClient,
            CourseAssignmentMapper mapper,
            CourseAssignmentRepository courseAssignmentRepository
    ) {
        this.batchServiceClient = batchServiceClient;
        this.trainerServiceClient = trainerServiceClient;
        this.courseTechnologyServiceClient = courseTechnologyServiceClient;
        this.mapper = mapper;
        this.courseAssignmentRepository = courseAssignmentRepository;
    }



    private void enrichCourseAssignmentResponses(List<CourseAssignmentResponse> responses) {

        if (responses.isEmpty()) {
            return;
        }

        //Extract unique ids
        Set<Long> batchIds = responses.stream()
                .map(response -> response.getBatchId())
                .collect(Collectors.toSet());
        Set<Long> trainerIds = responses.stream()
                .map(response -> response.getTrainerId())
                .collect(Collectors.toSet());
        Set<Long> courseTechnologyIds = responses.stream()
                .map(response -> response.getCourseTechnologyId())
                .collect(Collectors.toSet());

        // Bulk API calls
        List<BatchResponse> batches = batchServiceClient.getBatchesByIds(new ArrayList<>(batchIds));
        System.out.println("Batch response: "+batches);
        List<TrainerResponse> trainers = trainerServiceClient.getTrainerByIds(new ArrayList<>(trainerIds));
        System.out.println("Trainer response: "+trainers);
        List<CourseTechnologyResponse> courseTechnologies = courseTechnologyServiceClient.getCourseTechnologiesByIds(new ArrayList<>(courseTechnologyIds));
        System.out.println("Course Technologies response: "+courseTechnologies);
        // Convert to lookup maps
        Map<Long,String> batchNameMap = new HashMap<>();

        for (BatchResponse batch : batches) {
            batchNameMap.put(
                    batch.getId(),
                    batch.getBatchName()
            );
        }

        Map<Long,String> trainerNameMap = new HashMap<>();

        for(TrainerResponse trainer : trainers) {
            trainerNameMap.put(
                    trainer.getId(),
                    trainer.getFirstName()
            );
        }

        Map<Long,String> courseTechnologyNameMap = new HashMap<>();

        for (CourseTechnologyResponse courseTechnology : courseTechnologies) {
            courseTechnologyNameMap.put(
                    courseTechnology.getId(),
                    courseTechnology.getTechnologyName()
            );
        }


        // Enrich DTOs
        for(CourseAssignmentResponse response : responses) {
            response.setBatchName(batchNameMap.get(response.getBatchId()));
            response.setTrainerName(trainerNameMap.get(response.getTrainerId()));
            response.setCourseTechnologyName(courseTechnologyNameMap.get(response.getCourseTechnologyId()));
        }
    }





    @Override
    public CourseAssignmentResponse createCourseAssignment(
            CourseAssignmentRequest request
    ) {


        log.info(
                "Creating course assignment. batchId={}, courseTechnologyId={}, trainerId={}",
                request.getBatchId(),
                request.getCourseTechnologyId(),
                request.getTrainerId()
        );

        BatchResponse batchResponse = batchServiceClient.getBatch(request.getBatchId());
        CourseTechnologyResponse courseTechnologyResponse = courseTechnologyServiceClient.getCourseTechnology(request.getCourseTechnologyId());
        TrainerResponse trainerResponse = trainerServiceClient.getEmployee(request.getTrainerId());

        if (trainerResponse.getDepartment() != EmployeeDepartment.INSTRUCTOR) {
            log.warn(
                    "Course assignment creation failed. " +
                            "Employee is not an instructor. trainerId={}, department={}",
                    request.getTrainerId(),
                    trainerResponse.getDepartment()
            );
            throw new InvalidAssignmentException("Employee is not an Instructor");
        }

        CourseAssignment assignment = mapper.toEntity(request);
        assignment.setStatus(AssignmentStatus.SCHEDULED);
        assignment.setBatchId(batchResponse.getId());
        assignment.setCourseTechnologyId(courseTechnologyResponse.getId());
        assignment.setTrainerId(trainerResponse.getId());

        CourseAssignment saved = courseAssignmentRepository.save(assignment);

        CourseAssignmentResponse response = mapper.toResponseDTO(saved);

        response.setBatchName(batchResponse.getBatchName());
        response.setCourseTechnologyName(courseTechnologyResponse.getTechnologyName());
        response.setTrainerName(trainerResponse.getFirstName());

        log.info(
                "Course assignment created successfully. " +
                        "assignmentId={}, batchId={}, courseTechnologyId={}, trainerId={}",
                saved.getId(),
                saved.getBatchId(),
                saved.getCourseTechnologyId(),
                saved.getTrainerId()
        );

        return response;

    }





    @Override
    public List<CourseAssignmentResponse> getAllCourseAssignment() {

        List<CourseAssignment> allCourseAssignments = courseAssignmentRepository.findAll();

        List<CourseAssignmentResponse> responses = mapper.toResponseDTOList(allCourseAssignments);

        enrichCourseAssignmentResponses(responses);

        return responses;
    }





    @Override
    public List<CourseAssignmentResponse> getAllCourseAssignmentByCourseTechnologyId(Long courseTechnologyId) {
        log.debug(
                "Fetching course assignments for courseTechnologyId={}",
                courseTechnologyId
        );
        courseTechnologyServiceClient.getCourseTechnology(courseTechnologyId);

        List<CourseAssignment> allCourseAssignmentsByCourseId = courseAssignmentRepository.findByCourseTechnologyId(courseTechnologyId);

        List<CourseAssignmentResponse> responses = mapper.toResponseDTOList(allCourseAssignmentsByCourseId);

        enrichCourseAssignmentResponses(responses);

        return  responses;
    }




    @Override
    public List<CourseAssignmentResponse> getAllCourseAssignmentByBatchId(Long batchId) {

        batchServiceClient.getBatch(batchId);

        List<CourseAssignment> allCourseAssignmentsByBatchId = courseAssignmentRepository.findByBatchId(batchId);

        List<CourseAssignmentResponse> responses = mapper.toResponseDTOList(allCourseAssignmentsByBatchId);

        enrichCourseAssignmentResponses(responses);

        return responses;
    }


    public List<CourseAssignmentResponse> getAllCourseAssignmentByTrainerId(Long trainerId) {

        trainerServiceClient.getEmployee(trainerId);

        List<CourseAssignment> allCourseAssignmentsByTrainerId = courseAssignmentRepository.findByTrainerId(trainerId);
        List<CourseAssignmentResponse> responses = mapper.toResponseDTOList(allCourseAssignmentsByTrainerId);
        enrichCourseAssignmentResponses(responses);
        return responses;
    }


    public CourseAssignmentResponse updateCourseAssignment(Long id, CourseAssignmentRequest request){

        log.info("Updating course assignment. assignmentId={}", id);

        CourseAssignment courseAssignment = courseAssignmentRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn(
                            "Course assignment updation failed. Course assignment not found. assignmentId={}",
                            id
                    );

                    return new ResourceNotFoundException("Course Assignment Not Found");
                });

        BatchResponse batchResponse = batchServiceClient.getBatch(request.getBatchId());
        CourseTechnologyResponse courseTechnologyResponse = courseTechnologyServiceClient.getCourseTechnology(request.getCourseTechnologyId());
        TrainerResponse trainerResponse = trainerServiceClient.getEmployee(request.getTrainerId());

        if (trainerResponse.getDepartment() != EmployeeDepartment.INSTRUCTOR) {

            log.warn(
                    "Invalid course assignment. Employee is not an instructor. " +
                            "assignmentId={}, trainerId={}, department={}",
                    id,
                    request.getTrainerId(),
                    trainerResponse.getDepartment()
            );

            throw new InvalidAssignmentException("Employee is not an Instructor");
        }

        mapper.toEntity(request);

        courseAssignment.setBatchId(batchResponse.getId());
        courseAssignment.setCourseTechnologyId(courseTechnologyResponse.getId());
        courseAssignment.setTrainerId(trainerResponse.getId());

        if (request.getStatus() != null){
            courseAssignment.setStatus(AssignmentStatus.SCHEDULED);
        }

        courseAssignmentRepository.save(courseAssignment);

        log.info(
                "Course assignment updated successfully. " +
                        "assignmentId={}, batchId={}, courseTechnologyId={}, trainerId={}",
                id,
                batchResponse.getId(),
                courseTechnologyResponse.getId(),
                trainerResponse.getId()
        );

        CourseAssignmentResponse response = mapper.toResponseDTO(courseAssignment);
        response.setBatchName(batchResponse.getBatchName());
        response.setCourseTechnologyName(courseTechnologyResponse.getTechnologyName());
        response.setTrainerName(trainerResponse.getFirstName());

        return response;


    }


    public CourseAssignmentResponse patchCourseAssignment(Long id, CourseAssignmentPatchRequest request){

        log.info("Patching course assignment. assignmentId={}", id);

        CourseAssignment courseAssignment = courseAssignmentRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn(
                            "Course assignment not found. assignmentId={}",
                            id
                    );

                    return new ResourceNotFoundException("Course Assignment Not Found");
                });

        BatchResponse batchResponse = batchServiceClient.getBatch(request.getBatchId());
        CourseTechnologyResponse courseTechnologyResponse = courseTechnologyServiceClient.getCourseTechnology(request.getCourseTechnologyId());
        TrainerResponse trainerResponse = trainerServiceClient.getEmployee(request.getTrainerId());


        mapper.patchEntity(request, courseAssignment);

        courseAssignment.setBatchId(batchResponse.getId());
        courseAssignment.setCourseTechnologyId(courseTechnologyResponse.getId());
        courseAssignment.setTrainerId(trainerResponse.getId());

        if (request.getStatus() != null){
            courseAssignment.setStatus(AssignmentStatus.SCHEDULED);
        }

        courseAssignmentRepository.save(courseAssignment);

        log.info(
                "Course assignment patched successfully. " +
                        "assignmentId={}, batchId={}, courseTechnologyId={}, trainerId={}",
                id,
                batchResponse.getId(),
                courseTechnologyResponse.getId(),
                trainerResponse.getId()
        );

        CourseAssignmentResponse response = mapper.toResponseDTO(courseAssignment);

        response.setCourseTechnologyName(courseTechnologyResponse.getTechnologyName());
        response.setBatchName(batchResponse.getBatchName());
        response.setTrainerName(trainerResponse.getFirstName());

        return response;
    }
}


