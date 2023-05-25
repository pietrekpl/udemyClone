package com.example.corneradmin.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseDto {

    private Long courseId;
    private String courseName;
    private String courseDuration;
    private String courseDescription;
    private InstructorDto instructor;
}
