package com.example.course_service.service;

import com.example.course_service.dto.CourseDetailResponse;
import com.example.course_service.dto.CourseDetailRow;
import com.example.course_service.dto.CourseRequest;
import com.example.course_service.dto.CourseResponse;

import java.util.List;

public interface CourseService {

    CourseResponse createCourse(CourseRequest request);
    List<CourseResponse> getAllCourse();
    CourseResponse getCourseById(Long id);
//    CourseResponse getCourseByCode(String courseCode);
    CourseResponse updateCourse(Long id,CourseRequest request);
    void deleteCourse(Long id);
    CourseResponse patchCourse(Long id,CourseRequest request);
    CourseDetailResponse getCourseDetails(Long courseId);
    CourseResponse getInternalCourseById(Long id);


}
