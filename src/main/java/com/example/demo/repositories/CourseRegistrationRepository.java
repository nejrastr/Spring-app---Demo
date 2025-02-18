package com.example.demo.repositories;

import com.example.demo.entities.courses.Course;
import com.example.demo.entities.student.CourseRegistration;
import com.example.demo.entities.student.Student;
import com.example.demo.model.GradeDto;
import com.example.demo.model.NumberOfCourseRegistrationsDto;
import com.example.demo.model.StudentGradesDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CourseRegistrationRepository extends JpaRepository<CourseRegistration, Long> {
    CourseRegistration findByStudentAndCourse(Student student, Course course);

    Page<CourseRegistration> findByStudentId(Long studentid, Pageable pageable);

    @Query("""
            SELECT cr.student FROM CourseRegistration cr WHERE cr.course = :course
            """)
    Page<Student> findStudentByCourse(@Param("course") Course course, Pageable pageable);

    @Query("""
            SELECT cr.student FROM CourseRegistration cr WHERE cr.course = :course AND cr.grade=:grade
            
            """)
    Page<Student> findStudentsByGradeAndCourse(@Param("course") Course course, @Param("grade") double grade, Pageable pageable);

    @Query
            ("""
                    SELECT cr.grade FROM CourseRegistration cr WHERE cr.student.id=:studentId
                    """)
    List<Integer> getGradesByStudentId(@Param("studentId") Long studentId);

    @Query("SELECT new com.example.demo.model.GradeDto (c.student.id, c.student.name, AVG(c.grade)) FROM CourseRegistration c WHERE c.grade IS NOT NULL GROUP BY c.student.id,c.student.name")
    List<GradeDto> getAverageGrades();

    @Query("SELECT new com.example.demo.model.GradeDto (c.student.id, c.student.name, AVG(coalesce( c.grade, 0))) FROM CourseRegistration c GROUP BY c.student.id,c.student.name")
    List<GradeDto> getAverageGradesWithNull();

    @Query("SELECT cr.course FROM CourseRegistration cr  WHERE cr.student.id = :studentId")
    Page<StudentGradesDto> findStudentGradesForCourses(@Param("studentId") Long studentId, Pageable pageable);

    @Query("SELECT cr FROM CourseRegistration cr WHERE cr.course.id = :courseId")
    List<CourseRegistration> findByCourseId(@Param("courseId") Long courseId);


    @Query("SELECT new com.example.demo.model.NumberOfCourseRegistrationsDto(COUNT(cr),cr.course.name) " +
            "FROM CourseRegistration cr " +
            "GROUP BY cr.course.id,cr.course.name")
    Page<NumberOfCourseRegistrationsDto> getNumberOfRegistrationsForEachCourse(Pageable pageable);
}