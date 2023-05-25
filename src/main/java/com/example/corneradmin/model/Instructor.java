package com.example.corneradmin.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "instructors")
public class Instructor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "instructor_id", nullable = false)
    private Long instructorId;
    @Basic
    @Column(name = "first_name", nullable = false, length = 45)
    private String firstName;
    @Basic
    @Column(name = "last_name", nullable = false, length = 45)
    private String lastName;
    @Basic
    @Column(name = "summary", nullable = false, length = 64)
    private String summary;

    @OneToMany(mappedBy = "instructor", fetch = FetchType.LAZY)
    private Set<Course> courses = new HashSet<>();


    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "user_id",
            referencedColumnName = "user_id",
            nullable = false)
    private User user;

    public Instructor(String firstName, String lastName, String summary, User user) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.summary = summary;
        this.user = user;
    }
}
