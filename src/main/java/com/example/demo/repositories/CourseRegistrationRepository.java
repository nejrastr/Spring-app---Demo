package com.example.demo.repositories;

import com.example.demo.entities.courses.Course;
import com.example.demo.entities.student.CourseRegistration;
import com.example.demo.entities.student.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface CourseRegistrationRepository extends JpaRepository<CourseRegistration, Long> {
    CourseRegistration findByStudentAndCourse(Student student, Course course);
    Set<CourseRegistration> findByStudentId(Long studentid);
}
