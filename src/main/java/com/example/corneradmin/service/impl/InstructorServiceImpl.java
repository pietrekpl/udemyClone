package com.example.corneradmin.service.impl;

import com.example.corneradmin.dto.InstructorDto;
import com.example.corneradmin.dto.UserDto;
import com.example.corneradmin.mapper.InstructorMapper;
import com.example.corneradmin.model.Course;
import com.example.corneradmin.model.Instructor;
import com.example.corneradmin.model.User;
import com.example.corneradmin.repository.InstructorRepository;
import com.example.corneradmin.service.CourseService;
import com.example.corneradmin.service.InstructorService;
import com.example.corneradmin.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class InstructorServiceImpl implements InstructorService {

    private final InstructorRepository instructorRepository;

    private final InstructorMapper instructorMapper;

    private final CourseService courseService;

    private final UserService userService;

    @Override
    public Instructor loadInstructorById(Long instructorId) {
        return instructorRepository.findById(instructorId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    @Override
    public Page<InstructorDto> findInstructorsByName(String name, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Instructor> fetchInstructorPage = instructorRepository.findInstructorsByName(name, pageRequest);
        return new PageImpl<>(fetchInstructorPage.getContent().stream()
                .map(instructorMapper::fromInstructor)
                .collect(Collectors.toList()), pageRequest, fetchInstructorPage.getTotalElements());
    }

    @Override
    public InstructorDto loadInstructorByEmail(String email) {
        return instructorMapper.fromInstructor(instructorRepository.findInstructorByEmail(email));
    }

    @Override
    public InstructorDto createInstructor(InstructorDto instructorDto) {
        User user = userService.createUser(instructorDto.getUser().getEmail(),
                instructorDto.getUser().getPassword());
        userService.assignRoleToUser(user.getEmail(), "Instructor");


        Instructor instructor = instructorMapper.fromInstructorDto(instructorDto);
        instructor.setUser(user);
        Instructor saved = instructorRepository.save(instructor);
        return instructorMapper.fromInstructor(saved);

    }

    @Override
    public InstructorDto updateInstructor(InstructorDto instructorDto) {
        Instructor loadedInstructor = instructorRepository.findById(instructorDto.getInstructorId())
                .orElseThrow(() -> new EntityNotFoundException("Instructor Not found"));
        Instructor instructor = instructorMapper.fromInstructorDto(instructorDto);
        instructor.setUser(loadedInstructor.getUser());
        instructor.setCourses(loadedInstructor.getCourses());
        Instructor updatedInstructor = instructorRepository.save(instructor);

        return instructorMapper.fromInstructor(updatedInstructor);
    }

    @Override
    public List<InstructorDto> fetchInstructors() {
        return instructorRepository.findAll()
                .stream()
                .map(instructorMapper::fromInstructor)
                .collect(Collectors.toList());
    }

    @Override
    public void removeInstructor(Long instructorId) {
        Instructor instructor = loadInstructorById(instructorId);
        for (Course course : instructor.getCourses()) {
            courseService.removeCourse(course.getCourseId());
        }
        instructorRepository.deleteById(instructorId);
    }
}
