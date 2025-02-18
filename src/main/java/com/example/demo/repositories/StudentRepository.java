package com.example.demo.repositories;

import com.example.demo.entities.courses.Course;
import com.example.demo.entities.student.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long>, JpaSpecificationExecutor<Student> {

    Optional<Student> findByEmail(String email);

    Student findById(Course courseId);

    @Query("SELECT cr.student.name FROM CourseRegistration cr WHERE cr.course.id=:courseId AND cr.grade=:grade")
    List<String> findByCourseIdAndGrade(@Param("courseId") Long courseId, @Param("grade") Integer grade);


}
