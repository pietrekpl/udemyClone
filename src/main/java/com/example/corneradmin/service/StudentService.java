package com.example.corneradmin.service;

import com.example.corneradmin.dto.StudentDto;
import com.example.corneradmin.model.Student;
import org.springframework.data.domain.Page;

public interface StudentService {

    Student loadStudentById(Long studentId);

    Page<StudentDto> loadStudentsByName(String name, int page, int size);

    StudentDto loadStudentByEmail(String email);

    StudentDto createStudent(StudentDto studentDto);

    StudentDto updateStudent(StudentDto studentDto);

    void removeStudent(Long studentId);



}
