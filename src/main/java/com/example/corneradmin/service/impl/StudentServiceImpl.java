package com.example.corneradmin.service.impl;


import com.example.corneradmin.dto.StudentDto;
import com.example.corneradmin.mapper.StudentMapper;
import com.example.corneradmin.model.Course;
import com.example.corneradmin.model.Student;
import com.example.corneradmin.model.User;
import com.example.corneradmin.repository.StudentRepository;
import com.example.corneradmin.service.StudentService;
import com.example.corneradmin.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    private final StudentMapper studentMapper;

    private final UserService userService;

    @Override
    public Student loadStudentById(Long studentId) {
        return studentRepository.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Student not found"));
    }

    @Override
    public Page<StudentDto> loadStudentsByName(String name, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Student> studentPage = studentRepository.findStudentByName(name, pageRequest);
        return new PageImpl<>(studentPage.getContent().stream()
                .map(studentMapper::fromStudent)
                .collect(Collectors.toList()), pageRequest, studentPage.getTotalElements());
    }

    @Override
    public StudentDto loadStudentByEmail(String email) {
        return studentMapper.fromStudent(studentRepository.findStudentByEmail(email));
    }

    @Override
    public StudentDto createStudent(StudentDto studentDto) {
        User user = userService.createUser(studentDto.getUser().getEmail(), studentDto.getUser().getPassword());
        userService.assignRoleToUser(studentDto.getUser().getEmail(), "Student");
        Student student = studentMapper.fromStudentDto(studentDto);
        student.setUser(user);
        Student saved = studentRepository.save(student);
        return studentMapper.fromStudent(saved);
    }

    @Override
    public StudentDto updateStudent(StudentDto studentDto) {
        Student loadedStudent = loadStudentById(studentDto.getStudentId());
        Student student = studentMapper.fromStudentDto(studentDto);
        student.setUser(loadedStudent.getUser());
        student.setCourses(loadedStudent.getCourses());
        Student saved = studentRepository.save(student);
        return studentMapper.fromStudent(saved);
    }

    @Override
    public void removeStudent(Long studentId) {
    Student student = loadStudentById(studentId);
        Iterator<Course> courseIterator = student.getCourses().iterator();
        if (courseIterator.hasNext()){
            Course course = courseIterator.next();
            course.removeStudentFromCourse(student);
        }
        studentRepository.deleteById(studentId);
    }
}
