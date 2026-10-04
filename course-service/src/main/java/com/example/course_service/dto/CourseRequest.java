package com.example.course_service.dto;

import com.example.course_service.enums.CourseStatus;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


import java.math.BigDecimal;

public class CourseRequest {

    @NotBlank(message="Course Code is required")
    private String courseCode;

    @NotBlank(message="Course Name is required")
    private String courseName;

    @NotBlank(message="Course description is required")
    private String description;

    @NotNull
    @Min(1)
    private Integer duration;

    @NotNull
    @DecimalMin(value = "0.0")
    private BigDecimal fee;

    @NotBlank(message="Course category is required")
    private String category;

    private CourseStatus status;



    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public CourseStatus getStatus() {
        return status;
    }

    public void setStatus(CourseStatus status) {
        this.status = status;
    }

}
