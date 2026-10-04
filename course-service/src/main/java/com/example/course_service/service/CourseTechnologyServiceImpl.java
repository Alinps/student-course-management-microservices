package com.example.course_service.service;

import com.example.course_service.dto.CourseTechnologyPatchRequest;
import com.example.course_service.dto.CourseTechnologyRequest;
import com.example.course_service.dto.CourseTechnologyResponse;
import com.example.course_service.exception.InactiveResourceException;
import com.example.course_service.exception.ResourceAlreadyExistException;
import com.example.course_service.exception.ResourceNotFoundException;
import com.example.course_service.mapper.CourseTechnologyMapper;
import com.example.course_service.models.Course;
import com.example.course_service.models.CourseTechnology;
import com.example.course_service.models.Technology;
import com.example.course_service.repository.CourseRepository;
import com.example.course_service.repository.CourseTechnologyRepository;
import com.example.course_service.repository.TechnologyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseTechnologyServiceImpl implements  CourseTechnologyService{

    private final CourseTechnologyRepository courseTechnologyRepository;
    private final CourseRepository courseRepository;
    private final TechnologyRepository technologyRepository;
    private final CourseTechnologyMapper mapper;
    private static final Logger log = LoggerFactory.getLogger(CourseTechnologyServiceImpl.class);

    public CourseTechnologyServiceImpl(
            CourseTechnologyRepository courseTechnologyRepository,
            CourseTechnologyMapper mapper,
            CourseRepository courseRepository,
            TechnologyRepository technologyRepository
    ) {
        this.courseTechnologyRepository = courseTechnologyRepository;
        this.mapper = mapper;
        this.courseRepository = courseRepository;
        this.technologyRepository = technologyRepository;
    }

    @Override
    public CourseTechnologyResponse createCourseTechnology (
            Long courseId,
            CourseTechnologyRequest request
    ) {

        log.info(
                "Adding technology to course. courseId={}, technologyId={}",
                courseId,
                request.getTechnologyId()
        );

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> {

                    log.warn(
                            "Course technology creation failed. Course not found. courseId={}",
                            courseId
                    );

                    return new ResourceNotFoundException("Course not found");
                });

        Technology technology = technologyRepository.findById(request.getTechnologyId())
                .orElseThrow(() -> {

                    log.warn(
                            "Course technology creation failed. Technology not found. technologyId={}",
                            request.getTechnologyId()
                    );

                    return new ResourceNotFoundException("Technology not found");
                });
        if (!technology.getActive()) {

            log.warn(
                    "Course technology creation rejected. Technology is inactive. " +
                            "courseId={}, technologyId={}",
                    courseId,
                    technology.getId()
            );

            throw new InactiveResourceException("Can't add inactive technology");
        }

        if (courseTechnologyRepository.existsByCourseAndTechnology(course,technology)) {

            log.warn(
                    "Course technology creation rejected. Technology already assigned. " +
                            "courseId={}, technologyId={}",
                    courseId,
                    technology.getId()
            );

            throw new ResourceAlreadyExistException("Technology already added to this course");
        }

        CourseTechnology entity = mapper.toEntity(request);

        entity.setCourse(course);
        entity.setTechnology(technology);

        if (request.getOptional() == null) {
            entity.setOptional(true);
        }

        CourseTechnology saved = courseTechnologyRepository.save(entity);


        log.info(
                "Technology added to course successfully. courseId={}, technologyId={}, " +
                        "courseTechnologyId={}, optional={}",
                courseId,
                technology.getId(),
                saved.getId(),
                saved.getOptional()
        );


        return mapper.toResponseDTO(saved);
    }

    @Override
    public List<CourseTechnologyResponse> getTechnologiesByCourse(Long courseId) {

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> {

                    log.warn("Failed to get course. course not found. technologyId={}", courseId);

                    return new ResourceNotFoundException("Course Not found");
                });

        List<CourseTechnology> technologies = courseTechnologyRepository.findByCourse(course);

        return mapper.toResponseDTOList(technologies);

    }


    @Override
    public CourseTechnologyResponse getCourseTechnologyById(Long id) {
        CourseTechnology courseTechnology = courseTechnologyRepository.findById(id)
                .orElseThrow(() ->{

                    log.warn("Failed to get technology. technology not found. technologyId={}", id);
                    return new ResourceNotFoundException("Course Technology not found");

                });


        return mapper.toResponseDTO(courseTechnology);
    }

    @Override
    public CourseTechnologyResponse updateCourseTechnology(
            Long id,
            CourseTechnologyRequest request) {

        log.info(
                "Updating course technology. courseTechnologyId={}, technologyId={}",
                id,
                request.getTechnologyId()
        );



        CourseTechnology courseTechnology = courseTechnologyRepository.findById(id)
                .orElseThrow(() -> {

                    log.warn("Course technology update failed. Course technology not found. courseTechnologyId={}",id);

                    return new ResourceNotFoundException("Course Technology not found");
                });


        Technology technology = technologyRepository.findById(request.getTechnologyId())
                .orElseThrow(() -> {

                    log.warn("Course technology update failed. Technology not found. technologyId={}", request.getTechnologyId());

                    return new ResourceNotFoundException("Technology not found");
                });


        if (!courseTechnology.getTechnology().getId().equals(technology.getId())
        && courseTechnologyRepository.existsByCourseAndTechnology(courseTechnology.getCourse(),technology)) {

            log.warn(
                    "Course technology update rejected. " +
                            "Technology already exists in course. " +
                            "courseTechnologyId={}, courseId={}, technologyId={}",
                    id,
                    courseTechnology.getCourse().getId(),
                    technology.getId()
            );

            throw new ResourceAlreadyExistException("Technology already exists in this course");

        }

        mapper.updateEntity(request, courseTechnology);
        courseTechnology.setTechnology(technology);

        CourseTechnology updated = courseTechnologyRepository.save(courseTechnology);

        log.info(
                "Course technology updated successfully. " +
                        "courseTechnologyId={}, courseId={}, technologyId={}",
                updated.getId(),
                updated.getCourse().getId(),
                updated.getTechnology().getId()
        );

        return mapper.toResponseDTO(updated);
    }


    @Override
    public CourseTechnologyResponse patchCourseTechnology(
            Long id,
            CourseTechnologyPatchRequest request
    ) {


        log.info(
                "Patching course technology. courseTechnologyId={}, technologyId={}",
                id,
                request.getTechnologyId()
        );


        CourseTechnology courseTechnology = courseTechnologyRepository.findById(id)
                .orElseThrow(() -> {

                    log.warn("Course technology patch failed. Course technology not found. courseTechnologyId={}", id);

                    return new ResourceNotFoundException("Course Technology not found");
                });

        if (request.getTechnologyId() != null) {

            Technology technology = technologyRepository.findById(request.getTechnologyId())
                    .orElseThrow(() -> {

                        log.warn("Course technology patch failed. Technology not found. technologyId={}", request.getTechnologyId());

                        return new ResourceNotFoundException("Technology not found");
                    });

            if (!courseTechnology.getTechnology().getId().equals(request.getTechnologyId())
                    && courseTechnologyRepository.existsByCourseAndTechnology(courseTechnology.getCourse(), technology)) {

                log.warn(
                        "Course technology patch rejected. " +
                                "Technology already exists in course. " +
                                "courseTechnologyId={}, courseId={}, technologyId={}",
                        id,
                        courseTechnology.getCourse().getId(),
                        technology.getId()
                );

                throw new ResourceAlreadyExistException("Technology already exists.");

            }

            courseTechnology.setTechnology(technology);
    }
        mapper.pathEntity(request,courseTechnology);

        CourseTechnology updated = courseTechnologyRepository.save(courseTechnology);

        log.info(
                "Course technology patched successfully. courseTechnologyId={}, courseId={}, technologyId={}",
                updated.getId(),
                updated.getCourse().getId(),
                updated.getTechnology().getId()
        );


        return mapper.toResponseDTO(updated);

}

    @Override
    public void deleteCourseTechnology(Long id) {

        log.info("Deleting course technology courseTechnologyId={}", id);

        CourseTechnology courseTechnology = courseTechnologyRepository.findById(id)
                .orElseThrow(() -> {

                    log.warn("Course technology delete failed. Course technology not found. courseTechnologyId={}", id);
                    return new ResourceNotFoundException("Course Technology not found");
                });

        courseTechnology.setOptional(false);

        courseTechnologyRepository.save(courseTechnology);

    }



    @Override
    public List<CourseTechnologyResponse> getCourseTechnologiesById(List<Long> ids) {
        List<CourseTechnology> courseTechnologies = courseTechnologyRepository.findAllById(ids);
        return mapper.toResponseDTOList(courseTechnologies);
    }



}
