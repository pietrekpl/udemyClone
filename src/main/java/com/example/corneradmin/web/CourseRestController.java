package com.example.corneradmin.web;

import com.example.corneradmin.dto.CourseDto;
import com.example.corneradmin.service.CourseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/courses")
@CrossOrigin(origins = "*")
@Slf4j
public class CourseRestController {

    private final CourseService courseService;

    @PreAuthorize("hasAuthority('Admin')")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<CourseDto> searchCourses(@RequestParam(value = "keyword", defaultValue = "") String keyword,
                                         @RequestParam(value = "page", defaultValue = "0") int page,
                                         @RequestParam(value = "size", defaultValue = "5") int size) {
        return courseService.findCoursesByCourseName(keyword, page, size);
    }

    @PreAuthorize("hasAuthority('Admin')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{courseId}")
    public void deleteCourse(@PathVariable("courseId") Long courseId) {
        courseService.removeCourse(courseId);
    }

    @PreAuthorize("hasAnyAuthority('Admin','Instructor')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public CourseDto createCourse(@RequestBody CourseDto courseDto) {
        return courseService.createCourse(courseDto);
    }

    @PreAuthorize("hasAnyAuthority('Admin','Instructor')")
    @PutMapping("/{courseId}")
    @ResponseStatus(HttpStatus.OK)
    public CourseDto updateCourse(@RequestBody CourseDto courseDto,
                                  @PathVariable("courseId") Long courseId) {
        courseDto.setCourseId(courseId);
        return courseService.updateCourse(courseDto);
    }


    @PreAuthorize("hasAuthority('Student')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{courseId}/enroll/students/{studentId}")
    public void assignCourseToStudent(@PathVariable("courseId") Long courseId,
                                      @PathVariable("studentId") Long studentId) {

        log.info("COURSE ID {}", courseId);
        log.info("STUDENT ID {}", studentId);
        courseService.assignStudentToCourse(courseId, studentId);
    }


}
