package com.example.corneradmin.mapper;

import com.example.corneradmin.dto.StudentDto;
import com.example.corneradmin.model.Student;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class StudentMapper {

    public StudentDto fromStudent(Student student) {
        StudentDto studentDto = new StudentDto();
        BeanUtils.copyProperties(student, studentDto);
        return studentDto;
    }

    public Student fromStudentDto(StudentDto studentDto) {
        Student student = new Student();
        BeanUtils.copyProperties(studentDto, student);
        return student;
    }
}
