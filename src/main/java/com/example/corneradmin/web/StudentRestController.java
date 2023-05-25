package com.example.corneradmin.web;


import com.example.corneradmin.dto.CourseDto;
import com.example.corneradmin.dto.StudentDto;
import com.example.corneradmin.model.Course;
import com.example.corneradmin.model.User;
import com.example.corneradmin.service.CourseService;
import com.example.corneradmin.service.StudentService;
import com.example.corneradmin.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/students")
@CrossOrigin(origins = "*")
public class StudentRestController {

    private final StudentService studentService;

    private final UserService userService;

    private final CourseService courseService;


    @PreAuthorize("hasAuthority('Admin')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public Page<StudentDto> searchStudents(@RequestParam(name = "keyword", defaultValue = "") String keyword,
                                           @RequestParam(name = "page", defaultValue = "0") int page,
                                           @RequestParam(name = "size", defaultValue = "10") int size) {
        return studentService.loadStudentsByName(keyword, page, size);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{studentId}")
    @PreAuthorize("hasAuthority('Admin')")
    public void deleteStudent(@PathVariable("studentId") Long studentId) {
        studentService.removeStudent(studentId);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @PreAuthorize("hasAuthority('Admin')")
    public StudentDto createStudent(@RequestBody StudentDto studentDto) {
        User user = userService.loadUserByEmail(studentDto.getUser().getEmail());
        if (user != null) {
            throw new RuntimeException("Email already exists");
        }
        return studentService.createStudent(studentDto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{studentId}")
    @PreAuthorize("hasAuthority('Student')")
    public StudentDto updateStudent(@PathVariable("studentId") Long studentId,
                                    @RequestBody StudentDto studentDto) {
        studentDto.setStudentId(studentId);
        return studentService.updateStudent(studentDto);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{studentId}/courses")
    @PreAuthorize("hasAuthority('Student')")
    public Page<CourseDto> coursesByStudentId(@PathVariable("studentId") Long studentId,
                                              @RequestParam(name = "page", defaultValue = "0") int page,
                                              @RequestParam(name = "size", defaultValue = "10") int size) {
        return courseService.fetchCoursesForStudent(studentId, page, size);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{studentId}/other-courses")
    @PreAuthorize("hasAuthority('Student')")
    public Page<CourseDto> nonSubscribedCoursesByStudentId(@PathVariable("studentId") Long studentId,
                                                           @RequestParam(name = "page", defaultValue = "0") int page,
                                                           @RequestParam(name = "size", defaultValue = "10") int size) {
        return courseService.fetchNonEnrolledInCoursesForStudent(studentId, page, size);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/find")
    @PreAuthorize("hasAuthority('Student')")
    public StudentDto loadByEmail(@RequestParam(name = "email", defaultValue = "") String email) {
        return studentService.loadStudentByEmail(email);
    }


}
