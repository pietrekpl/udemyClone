package com.example.corneradmin.mapper;


import com.example.corneradmin.dto.InstructorDto;
import com.example.corneradmin.model.Instructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class InstructorMapper {

    public InstructorDto fromInstructor(Instructor instructor) {
        InstructorDto instructorDto = new InstructorDto();
        BeanUtils.copyProperties(instructor, instructorDto);
        return instructorDto;
    }

    public Instructor fromInstructorDto(InstructorDto instructorDto) {
        Instructor instructor = new Instructor();
        BeanUtils.copyProperties(instructorDto, instructor);
        return instructor;
    }
}
