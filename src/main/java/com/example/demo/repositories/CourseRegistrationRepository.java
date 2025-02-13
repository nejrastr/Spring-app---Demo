package com.example.demo.repositories;

import com.example.demo.entities.courses.Course;
import com.example.demo.entities.student.CourseRegistration;
import com.example.demo.entities.student.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface CourseRegistrationRepository extends JpaRepository<CourseRegistration, Long> {
    CourseRegistration findByStudentAndCourse(Student student, Course course);

    Set<CourseRegistration> findByStudentId(Long studentid);

    @Query("""
            SELECT cr.student FROM CourseRegistration cr WHERE cr.course = :course
            """)
    Set<Student> findStudentByCourse(@Param("course") Course course);

    @Query("""
            SELECT cr.student FROM CourseRegistration cr WHERE cr.course = :course AND cr.grade=:grade
            
            """)
    Set<Student> findStudentsByGradeAndCourse(@Param("course") Course course, @Param("grade") double grade);

    @Query
            ("""
                    SELECT cr.grade FROM CourseRegistration cr WHERE cr.student.id=:studentId
                    """)
    List<Integer> getGradesByStudentId(@Param("studentId") Long studentId);


}
