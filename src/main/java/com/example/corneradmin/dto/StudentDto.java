package com.example.corneradmin.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentDto {

    private Long studentId;
    private String firstName;
    private String lastName;
    private String level;
    private UserDto user;
}
