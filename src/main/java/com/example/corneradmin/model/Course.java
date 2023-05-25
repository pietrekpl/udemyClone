package com.example.corneradmin.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id", nullable = false)
    private Long courseId;
    @Column(name = "course_name", nullable = false, length = 45)
    private String courseName;
    @Column(name = "course_duration", nullable = false)
    private String courseDuration;
    @Column(name = "course_description", nullable = false, length = 64)
    private String courseDescription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instructor_id",
    referencedColumnName = "instructor_id")
    private Instructor instructor;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "enrolled_in",
    joinColumns = {@JoinColumn(name = "course_id")},
    inverseJoinColumns = {@JoinColumn(name = "student_id")}
    )
    private Set<Student> students = new HashSet<>();

    public Course(String courseName, String courseDuration,
                  String courseDescription, Instructor instructor) {
        this.courseName = courseName;
        this.courseDuration = courseDuration;
        this.courseDescription = courseDescription;
        this.instructor = instructor;
    }

    public void assignStudentToCourse(Student student) {
        this.students.add(student);
        student.getCourses().add(this);
    }

    public void removeStudentFromCourse(Student student) {
        this.students.remove(student);
        student.getCourses().remove(this);
    }
}
