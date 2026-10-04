package com.example.course_service.service;

import com.example.course_service.dto.*;
import com.example.course_service.enums.CourseStatus;
import com.example.course_service.exception.ResourceAlreadyExistException;
import com.example.course_service.exception.ResourceNotFoundException;
import com.example.course_service.models.Course;
import com.example.course_service.repository.CourseRepository;
import com.example.course_service.repository.CourseTechnologyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CourseServiceImpl implements  CourseService {

    private final CourseRepository courseRepository;
    private final CourseTechnologyRepository courseTechnologyRepository;
    private static final Logger log = LoggerFactory.getLogger(CourseServiceImpl.class);

    public CourseServiceImpl(
            CourseRepository courseRepository,
            CourseTechnologyRepository courseTechnologyRepository ) {
        this.courseRepository = courseRepository;
        this.courseTechnologyRepository = courseTechnologyRepository;
    }

    @Override
    public CourseResponse createCourse(CourseRequest request) {


        log.info(
                "Creating course. courseCode={}, courseName={}",
                request.getCourseCode(),
                request.getCourseName()
        );
        
        // check if course already exists with name and code
       if(courseRepository.existsByCourseCode(request.getCourseCode()) ||
       courseRepository.existsByCourseName(request.getCourseName())) {

           log.warn(
                   "Course creation rejected. Course already exists. courseCode={}, courseName={}",
                   request.getCourseCode(),
                   request.getCourseName()
           );

           throw new ResourceAlreadyExistException("Course already exists with same course code or course name.");

       }

       Course course = new Course();


        
       // convert DTO to Entity
       course.setCourseCode(request.getCourseCode());
       course.setCourseName(request.getCourseName());
       course.setDescription(request.getDescription());
       course.setDuration(request.getDuration());
       course.setFee(request.getFee());
       course.setCategory(request.getCategory());
       if (request.getStatus() != null){
           course.setStatus(request.getStatus());
       }else {
           course.setStatus(CourseStatus.ACTIVE);
       }


       // save to database
       Course savedCourse = courseRepository.save(course);

        log.info(
                "Course created successfully. courseId={}, courseCode={}, courseName={}",
                savedCourse.getId(),
                savedCourse.getCourseCode(),
                savedCourse.getCourseName()
        );

       // convert Entity to DTO
       CourseResponse response = new CourseResponse();

       response.setId(savedCourse.getId());
       response.setCourseName(savedCourse.getCourseName());
       response.setCourseCode(savedCourse.getCourseCode());
       response.setDescription(savedCourse.getDescription());
       response.setDuration(savedCourse.getDuration());
       response.setFee(savedCourse.getFee());
       response.setCategory(savedCourse.getCategory());
       response.setStatus(savedCourse.getStatus());

       return response;

    }

    @Override
    public List<CourseResponse> getAllCourse() {

        // creating a list for storing all course objects
        List<Course> courses = courseRepository.findAll();
        
        //  creating a list for storing all course object as response
        List<CourseResponse> responses = new ArrayList<>();

        for (Course course: courses) {

            CourseResponse response = new CourseResponse();

            response.setId(course.getId());
            response.setCourseName(course.getCourseName());
            response.setCourseCode(course.getCourseCode());
            response.setDescription(course.getDescription());
            response.setDuration(course.getDuration());
            response.setFee(course.getFee());
            response.setCategory(course.getCategory());
            response.setStatus(course.getStatus());

            responses.add(response);

        }

        return responses;

    }

    @Override
    public CourseResponse getCourseById(Long id) {

        Course course = courseRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Course not found with id: " + id));

        CourseResponse response = new CourseResponse();

        response.setId(course.getId());
        response.setCourseName(course.getCourseName());
        response.setCourseCode(course.getCourseCode());
        response.setDescription(course.getDescription());
        response.setDuration(course.getDuration());
        response.setFee(course.getFee());
        response.setCategory(course.getCategory());
        response.setStatus(course.getStatus());

        return response;
    }


    @Override
    public CourseResponse getInternalCourseById(Long id) {

        Course course = courseRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Course not found with id: " + id));

        CourseResponse response = new CourseResponse();

        response.setId(course.getId());
        response.setCourseName(course.getCourseName());
        response.setCourseCode(course.getCourseCode());
        response.setDescription(course.getDescription());
        response.setDuration(course.getDuration());
        response.setFee(course.getFee());
        response.setCategory(course.getCategory());
        response.setStatus(course.getStatus());

        return response;
    }


    @Override
    public CourseResponse updateCourse(Long id, CourseRequest request) {

        log.info(
                "Updating Course. courseId={}, courseCode={}, courseName={}",
                id,
                request.getCourseCode(),
                request.getCourseName()
        );
        
        // check if course exist.
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> {

                    log.warn("Course update failed. Course not found. courseId={}", id);
                    return  new ResourceNotFoundException("Course not found with id: "+id);

                });

        
        
        // check if course name is already used by another course
        courseRepository.findByCourseName(request.getCourseName())
                .ifPresent(existingCourse -> {
                    if (!existingCourse.getId().equals(id)) {

                        log.warn(
                                "Course update rejected. Course name already exists. " +
                                        "courseId={}, courseName={}",
                                id,
                                request.getCourseName()
                        );

                        throw new ResourceAlreadyExistException("Course name already exist.");
                    }
                });


        // check if course code is already used by another course
        courseRepository.findByCourseCode(request.getCourseCode())
                .ifPresent(existingCourse -> {

                    if(!existingCourse.getId().equals(id)) {

                        log.warn(
                                "Course update rejected. Course code already exists. " +
                                        "courseId={}, courseCode={}",
                                id,
                                request.getCourseCode()
                        );

                        throw new ResourceAlreadyExistException("Course code already exist.");

                    }
                });

        
        // updating the course details
        course.setCourseCode(request.getCourseCode());
        course.setCourseName(request.getCourseName());
        course.setDescription(request.getDescription());
        course.setDuration(request.getDuration());
        course.setFee(request.getFee());
        course.setCategory(request.getCategory());
        course.setStatus(request.getStatus());

        // save updated course
        Course updatedCourse = courseRepository.save(course);

        log.info(
                "Course updated successfully. courseId={}, courseCode={}, courseName={}",
                updatedCourse.getId(),
                updatedCourse.getCourseCode(),
                updatedCourse.getCourseName()
        );
        
        // convert Entity to DTO
        CourseResponse response = new CourseResponse();

        response.setId(updatedCourse.getId());
        response.setCourseName(updatedCourse.getCourseName());
        response.setCourseCode(updatedCourse.getCourseCode());
        response.setDescription(updatedCourse.getDescription());
        response.setDuration(updatedCourse.getDuration());
        response.setFee(updatedCourse.getFee());
        response.setCategory(updatedCourse.getCategory());
        response.setStatus(updatedCourse.getStatus());
        
        return  response;
        
    }
    
    @Override
    public void deleteCourse(Long id) {

        log.info("Deleting Course. courseId={}", id);
        
        Course course =  courseRepository.findById(id)
                .orElseThrow(() -> {

                    log.warn("Course delete failed. Course not found. courseId={}", id);

                    return  new ResourceNotFoundException("Course not found with id: "+id);

                });

        
        courseRepository.delete(course);
        log.info(
                "Course deleted successfully. courseId={}, courseCode={}, courseName={}",
                course.getId(),
                course.getCourseCode(),
                course.getCourseName()
        );
    }


    @Override
    public CourseResponse patchCourse(Long id, CourseRequest request) {

        log.info(
                "Patching Course. courseId={}, courseCode={}, courseName={}",
                id,
                request.getCourseCode(),
                request.getCourseName()
        );

        // check if the course  exists.
        Course course = courseRepository.findById(id)
                .orElseThrow(() ->{

                    log.warn("Course patch failed. Course not found. courseId={}", id);

                    return new ResourceNotFoundException("Course not found with this id: "+id);
                });


        // check if code field is not empty
        if (request.getCourseCode() != null) {

            courseRepository.findByCourseCode(request.getCourseCode())
                            .ifPresent(existingCourse -> {
                                if (!existingCourse.getId().equals(id)) {


                                    log.warn(
                                            "Course patch rejected. Course name already exists. " +
                                                    "courseId={}, courseName={}",
                                            id,
                                            request.getCourseName()
                                    );

                                    throw new ResourceAlreadyExistException("Course code already exist.");
                                }
                            });
            course.setCourseCode(request.getCourseCode());

        }

        // check if  name field is not empty
        if (request.getCourseName() != null) {

            courseRepository.findByCourseName(request.getCourseName())
                    .ifPresent(existingCourse -> {
                        if (!existingCourse.getId().equals(id)) {


                            log.warn(
                                    "Course patch rejected. Course code already exists. " +
                                            "courseId={}, courseName={}",
                                    id,
                                    request.getCourseName()
                            );

                            throw new ResourceAlreadyExistException("Course name already exist.");
                        }
                    });
            course.setCourseName(request.getCourseName());
        }

        // check if description field is not empty
        if (request.getDescription() != null) {
            course.setDescription(request.getDescription());
        }

        // check if  duration field is not empty
        if (request.getDuration() != null) {
            course.setDuration(request.getDuration());
        }

        // check if fee field is not empty
        if (request.getFee() != null) {
            course.setFee(request.getFee());
        }

        // check if category field is not empty
        if (request.getCategory() != null) {
            course.setCategory(request.getCategory());
        }

        // check if status field is not empty
        if (request.getStatus() != null) {
            course.setStatus(request.getStatus());
        }

        // save updated course
        Course updatedCourse = courseRepository.save(course);

        log.info(
                "Course patched successfully. courseId={}, courseCode={}, courseName={}",
                course.getId(),
                course.getCourseCode(),
                course.getCourseName()
        );

        // convert Entity to DTO
        CourseResponse response = new CourseResponse();

        response.setId(updatedCourse.getId());
        response.setCourseName(updatedCourse.getCourseName());
        response.setCourseCode(updatedCourse.getCourseCode());
        response.setDescription(updatedCourse.getDescription());
        response.setDuration(updatedCourse.getDuration());
        response.setFee(updatedCourse.getFee());
        response.setCategory(updatedCourse.getCategory());
        response.setStatus(updatedCourse.getStatus());

        return response;




    }


    @Override
    public CourseDetailResponse getCourseDetails(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Course not found"));

        List<CourseDetailRow> rows = courseTechnologyRepository.findCourseDetail(courseId);

        CourseDetailResponse response = new CourseDetailResponse();

        response.setId(course.getId());
        response.setCourseCode(course.getCourseCode());
        response.setCourseName(course.getCourseName());
        response.setDescription(course.getDescription());
        response.setDuration(course.getDuration());
        response.setFee(course.getFee());
        response.setCategory(course.getCategory());
        response.setStatus(course.getStatus());

        /*
        HashMap stores key-value pairs without guaranteeing iteration order,
        whereas LinkedHashMap extends HashMap and maintains a predictable iteration
        order, normally insertion order. LinkedHashMap achieves this using a doubly
        linked list in addition to the hash table.
         */
        Map<Long, CourseTechnologyDetailResponse> technologyMap = new LinkedHashMap<>();

        for (CourseDetailRow row:rows) {

            CourseTechnologyDetailResponse technologyResponse =
                    /*
                    Look for this technology in the map.
                    If it doesn't exist, create it.
                    Then give the technology DTO.
                     */
                    technologyMap.computeIfAbsent(
                            row.technologyId(),
                            id -> {
                                CourseTechnologyDetailResponse dto = new CourseTechnologyDetailResponse();
                                dto.setTechnologyId(id);
                                dto.setTechnologyName(row.technologyName());
                                dto.setDisplayOrder(row.displayOrder());
                                dto.setDuration(row.durationDays());
                                dto.setOptional(row.optional());
                                dto.setDays(new ArrayList<>());
                                return  dto;
                            });

            if (row.dayId() != null) {

                TechnologyDayDetailResponse dayResponse = new TechnologyDayDetailResponse();
                dayResponse.setId(row.dayId());
                dayResponse.setDayNumber(row.dayNumber());
                dayResponse.setTitle(row.dayTitle());
                dayResponse.setDescription(row.dayDescription());
                dayResponse.setEstimatedHours(row.estimatedHours());

                technologyResponse.getDays().add(dayResponse);
            }
        }

        response.setTechnologies(new ArrayList<>(technologyMap.values()));
        return response;
    }


    
}
