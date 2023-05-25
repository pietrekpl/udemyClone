package com.example.corneradmin.service;

import com.example.corneradmin.dto.InstructorDto;
import com.example.corneradmin.model.Instructor;
import org.springframework.data.domain.Page;

import java.util.List;

public interface InstructorService {

    Instructor loadInstructorById(Long instructorId);

    Page<InstructorDto> findInstructorsByName(String name, int page, int size);

    InstructorDto loadInstructorByEmail(String email);

    InstructorDto createInstructor(InstructorDto instructorDto);

    InstructorDto updateInstructor(InstructorDto instructorDto);

    List<InstructorDto> fetchInstructors();

    void removeInstructor(Long instructorId);






}
