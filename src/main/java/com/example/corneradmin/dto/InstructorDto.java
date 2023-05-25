package com.example.corneradmin.dto;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class InstructorDto {
    private Long instructorId;
    private String firstName;
    private String lastName;
    private String summary;
    private UserDto user;
}
