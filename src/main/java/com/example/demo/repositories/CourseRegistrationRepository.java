package com.example.demo.repositories;

import com.example.demo.entities.courses.Course;
import com.example.demo.entities.student.CourseRegistration;
import com.example.demo.entities.student.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface CourseRegistrationRepository extends JpaRepository<CourseRegistration, Long> {
    CourseRegistration findByStudentAndCourse(Student student, Course course);
    Set<CourseRegistration> findByStudentId(Long studentid);

    @Query("""
            SELECT cr.student FROM CourseRegistration cr
                   where cr.course = :course
            """)
    Page<Student> findByCourse(@Param("course") Course course, Pageable pageable);




}
