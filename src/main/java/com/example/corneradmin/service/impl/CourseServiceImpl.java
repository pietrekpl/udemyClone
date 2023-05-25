package com.example.corneradmin.service.impl;

import com.example.corneradmin.dto.CourseDto;
import com.example.corneradmin.mapper.CourseMapper;
import com.example.corneradmin.model.Course;
import com.example.corneradmin.model.Instructor;
import com.example.corneradmin.model.Student;
import com.example.corneradmin.repository.CourseRepository;
import com.example.corneradmin.repository.InstructorRepository;
import com.example.corneradmin.repository.StudentRepository;
import com.example.corneradmin.service.CourseService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    private final StudentRepository studentRepository;

    private final InstructorRepository instructorRepository;

    @Override
    public Course loadCourseById(Long courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));
    }

    @Override
    public CourseDto createCourse(CourseDto courseDto) {
        Course course = courseMapper.fromCourseDto(courseDto);
        Instructor instructor = instructorRepository.findById(courseDto.getInstructor().getInstructorId())
                .orElseThrow(() -> new EntityNotFoundException("Instructor not found"));
        course.setInstructor(instructor);
        Course save = courseRepository.save(course);
        return courseMapper.fromCourse(save);

    }

    @Override
    public CourseDto updateCourse(CourseDto courseDto) {
        Course loadedCourse = loadCourseById(courseDto.getCourseId());
        Instructor instructor = instructorRepository.findById(courseDto.getInstructor().getInstructorId())
                .orElseThrow(() -> new EntityNotFoundException("Instructor not found"));
        Course course = courseMapper.fromCourseDto(courseDto);
        course.setInstructor(instructor);
        course.setStudents(loadedCourse.getStudents());
        Course updatedCourse = courseRepository.save(course);

        return courseMapper.fromCourse(updatedCourse);
    }

    @Override
    public Page<CourseDto> findCoursesByCourseName(String keyword, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Course> coursesPage = courseRepository.findCoursesByCourseNameContains(keyword, pageRequest);
        return new PageImpl<>(coursesPage.getContent().stream()
                .map(courseMapper::fromCourse)
                .collect(Collectors.toList()), pageRequest, coursesPage.getTotalElements());
    }

    @Override
    public void assignStudentToCourse(Long courseId, Long studentId) {
    Student student = studentRepository.findById(studentId)
            .orElseThrow(() -> new EntityNotFoundException("Student not found"));
    Course course = loadCourseById(courseId);
    course.assignStudentToCourse(student);
    }

    @Override
    public Page<CourseDto> fetchCoursesForStudent(Long studentId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Course> studentCoursesPage = courseRepository.getCoursesByStudentId(studentId, pageRequest);
        return new PageImpl<>(studentCoursesPage.getContent().stream()
                .map(courseMapper::fromCourse)
                .collect(Collectors.toList()), pageRequest, studentCoursesPage.getTotalElements());
    }

    @Override
    public Page<CourseDto> fetchNonEnrolledInCoursesForStudent(Long studentId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Course> nonEnrolledInCoursesPage = courseRepository.getNonEnrolledInCoursesByStudentID(studentId, pageRequest);
        return new PageImpl<>(nonEnrolledInCoursesPage.getContent().stream()
                .map(courseMapper::fromCourse)
                .collect(Collectors.toList()), pageRequest, nonEnrolledInCoursesPage.getTotalElements());
    }

    @Override
    public void removeCourse(Long courseId) {
        courseRepository.deleteById(courseId);

    }

    @Override
    public Page<CourseDto> fetChCoursesFromInstructor(Long instructorId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Course> fetChCoursesPage = courseRepository.getCoursesByInstructorId(instructorId, pageRequest);
        return new PageImpl<>(fetChCoursesPage.getContent().stream()
                .map(courseMapper::fromCourse)
                .collect(Collectors.toList()), pageRequest, fetChCoursesPage.getTotalElements());
    }
}
