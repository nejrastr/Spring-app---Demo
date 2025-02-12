package com.example.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.example.demo.entities.courses.Course;



@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    Object findCourseById(Long subjectId);

    Course findById(Course courseId);

}
