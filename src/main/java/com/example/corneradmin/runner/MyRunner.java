package com.example.corneradmin.runner;


import com.example.corneradmin.dto.CourseDto;
import com.example.corneradmin.dto.InstructorDto;
import com.example.corneradmin.dto.StudentDto;
import com.example.corneradmin.dto.UserDto;
import com.example.corneradmin.mapper.StudentMapper;
import com.example.corneradmin.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class MyRunner implements CommandLineRunner {

    private final RoleService roleService;

    private final CourseService courseService;

    private final StudentService studentService;

    private final StudentMapper studentMapper;

    private final UserService userService;
    private final InstructorService instructorService;

    @Override
    public void run(String... args) {
        createRoles();
        createAdmin();
        createInstructors();
        createCourses();
        StudentDto studentDto =  createStudent();
        assignCourseToStudent(studentDto);
        createStudents();
    }

    private void createStudents() {

        for (int i = 1; i <= 10; i++) {
            StudentDto studentDto = new StudentDto();
            studentDto.setFirstName("StudentFN"+i);
            studentDto.setLastName("StudentLN"+i);
            studentDto.setLevel("Beginner"+i);

            UserDto userDto = new UserDto();
            userDto.setEmail("students+" +i+ "@gmail.com");
            userDto.setPassword("1234");
            studentDto.setUser(userDto);

             studentService.createStudent(studentDto);
        }



    }

    private void assignCourseToStudent(StudentDto studentDto) {
       courseService.assignStudentToCourse(1L, studentDto.getStudentId());
    }

    private StudentDto createStudent() {

        StudentDto studentDto = new StudentDto();
        studentDto.setFirstName("StudentFN");
        studentDto.setLastName("StudentLN");
        studentDto.setLevel("Beginner");

        UserDto userDto = new UserDto();
        userDto.setEmail("studenty@gmail.com");
        userDto.setPassword("1234");
        studentDto.setUser(userDto);

        return studentService.createStudent(studentDto);
    }

    private void createCourses() {
        for (int i = 0; i < 20; i++) {
            CourseDto courseDto = new CourseDto();
            courseDto.setCourseDescription("Description"+i);
            courseDto.setCourseDuration("1 h");
            courseDto.setCourseName("Course"+i);
            InstructorDto instructorDto = new InstructorDto();
            instructorDto.setInstructorId(1L);
            courseDto.setInstructor(instructorDto);
            courseService.createCourse(courseDto);
        }
    }

    private void createInstructors() {
        for (int i = 0; i < 10; i++) {

            InstructorDto instructorDto = new InstructorDto();
            instructorDto.setFirstName("Instructor"+i+"FN");
            instructorDto.setLastName("Instructor"+i+"LN");
            instructorDto.setSummary("Summary: "+i+".");

            UserDto userDto = new UserDto();
            userDto.setEmail("instructor"+i+"@gmail.com");
            userDto.setPassword("pass1234");
            instructorDto.setUser(userDto);

            instructorService.createInstructor(instructorDto);

        }
    }

    private void createAdmin() {
        userService.createUser("admin@gmail.com", "admin1234");
        userService.assignRoleToUser("admin@gmail.com", "Admin");
    }

    private void createRoles() {
        Arrays.asList("Admin", "Instructor", "Student").forEach(roleService::createRole);
    }
}
