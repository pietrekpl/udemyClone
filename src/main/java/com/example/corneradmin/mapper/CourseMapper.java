package com.example.corneradmin.mapper;

import com.example.corneradmin.dto.CourseDto;
import com.example.corneradmin.model.Course;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CourseMapper {

    private final InstructorMapper instructorMapper;

    public CourseDto fromCourse(Course course) {
        CourseDto courseDto = new CourseDto();
        BeanUtils.copyProperties(course, courseDto);
        courseDto.setInstructor(instructorMapper.fromInstructor(course.getInstructor()));
        return courseDto;
    }

    public Course fromCourseDto(CourseDto courseDto) {
        Course course = new Course();
        BeanUtils.copyProperties(courseDto, course);
        return course;
    }
}
