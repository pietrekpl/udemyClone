package com.example.corneradmin.repository;

import com.example.corneradmin.model.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    Page<Course> findCoursesByCourseNameContains(String name, Pageable pageable);

    @Query(value = "select * from courses where course_id in (select e.course_id from enrolled_in as e where e.student_id=:studentId)", nativeQuery = true)
    Page<Course> getCoursesByStudentId(@Param("studentId") Long studentId, Pageable pageable);

    @Query(value = "select * from courses where course_id not in (select e.course_id from enrolled_in as e where e.student_id=:studentId)", nativeQuery = true)
    Page<Course> getNonEnrolledInCoursesByStudentID(@Param("studentId") Long studentId, Pageable pageable);

    @Query(value = "select c from Course as c where c.instructor.instructorId=:instructorId")
    Page<Course> getCoursesByInstructorId(@Param("instructorId") Long instructorId, Pageable pageable);
}
