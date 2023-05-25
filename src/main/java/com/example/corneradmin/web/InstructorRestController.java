package com.example.corneradmin.web;


import com.example.corneradmin.dto.CourseDto;
import com.example.corneradmin.dto.InstructorDto;
import com.example.corneradmin.model.Instructor;
import com.example.corneradmin.model.User;
import com.example.corneradmin.service.CourseService;
import com.example.corneradmin.service.InstructorService;
import com.example.corneradmin.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/instructors")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Slf4j
public class InstructorRestController {

    private final InstructorService instructorService;
    private final CourseService courseService;

    private final UserService userService;



    @PreAuthorize("hasAuthority('Admin')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public InstructorDto createInstructor(@RequestBody InstructorDto instructorDto) {
        User user = userService.loadUserByEmail(instructorDto.getUser().getEmail());
        if (user != null) throw new RuntimeException("Email already exists");
        return instructorService.createInstructor(instructorDto);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping()
    @PreAuthorize("hasAuthority('Admin')")
    public Page<InstructorDto> searchInstructor(@RequestParam(name = "keyword", defaultValue = "") String keyword,
                                                @RequestParam(name = "page", defaultValue = "0") int page,
                                                @RequestParam(name = "size", defaultValue = "5") int size) {
        return instructorService.findInstructorsByName(keyword, page, size);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/all")
    @PreAuthorize("hasAuthority('Admin')")
    public List<InstructorDto> fetchAllInstructors() {
        return instructorService.fetchInstructors();
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{instructorId}")
    public void deleteInstructor(@PathVariable("instructorId") Long instructorId) {
        instructorService.removeInstructor(instructorId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{instructorId}/courses")
    @PreAuthorize("hasAuthority('Instructor')")
    public Page<CourseDto> fetchById(@PathVariable("instructorId") Long instructorId,
                                     @RequestParam(name = "page", defaultValue = "0") int page,
                                     @RequestParam(name = "size", defaultValue = "5") int size
    ) {
        return courseService.fetChCoursesFromInstructor(instructorId, page, size);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{instructorId}")
    @PreAuthorize("hasAuthority('Instructor')")
    public InstructorDto updateInstructor(@RequestBody InstructorDto instructorDto,
                                          @PathVariable("instructorId") Long instructorId) {
        instructorDto.setInstructorId(instructorId);
        return instructorService.updateInstructor(instructorDto);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/find")
    @PreAuthorize("hasAnyAuthority('Admin','Instructor')")
    public InstructorDto fetchInstructorByMail(@RequestParam(name = "email", defaultValue = "") String email) {
        return instructorService.loadInstructorByEmail(email);
    }

}
