package com.example.demo.repositories;

import com.example.demo.entities.courses.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    Object findCourseById(Long subjectId);

    Course findById(Course courseId);


    @Query("SELECT c FROM Course c " +
            "LEFT JOIN CourseRegistration cr ON c.id = cr.course.id " +
            "WHERE (:studentId IS NULL OR cr.student.id = :studentId) " +
            "AND (:professorId IS NULL OR cr.profesor.id = :professorId)")
    Page<Course> findCourseByStudentIdAndProfessorId(@Param("studentId") Long studentId, @Param("professorId") Long professorId, Pageable pageable);
}
