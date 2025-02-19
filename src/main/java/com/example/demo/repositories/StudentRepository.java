package com.example.demo.repositories;

import com.example.demo.entities.courses.Course;
import com.example.demo.entities.student.Student;
import com.example.demo.model.StudentDto;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long>, JpaSpecificationExecutor<Student> {

    Optional<Student> findByEmail(String email);

    Student findById(Course courseId);

    @Query("SELECT cr.student.name FROM CourseRegistration cr WHERE cr.course.id=:courseId AND cr.grade=:grade")
    Page<StudentDto> findByCourseIdAndGrade(@Param("courseId") Long courseId, @Param("grade") Integer grade, Pageable pageable);


    Optional<Student> findByName(@NotBlank(message = "Name can not be empty") String name);
}
