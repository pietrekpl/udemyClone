package com.example.corneradmin.service;

import com.example.corneradmin.dto.CourseDto;
import com.example.corneradmin.model.Course;
import org.springframework.data.domain.Page;

public interface CourseService {

    Course loadCourseById(Long courseId);

    CourseDto createCourse(CourseDto courseDto);

    CourseDto updateCourse(CourseDto courseDto);

    Page<CourseDto> findCoursesByCourseName(String keyword, int page, int size);

    void assignStudentToCourse(Long courseId, Long studentId);

    Page<CourseDto> fetchCoursesForStudent(Long studentId, int page, int size);

    Page<CourseDto> fetchNonEnrolledInCoursesForStudent(Long studentId, int page, int size);

    void removeCourse(Long courseId);

    Page<CourseDto> fetChCoursesFromInstructor(Long instructorId, int page, int size);

}
