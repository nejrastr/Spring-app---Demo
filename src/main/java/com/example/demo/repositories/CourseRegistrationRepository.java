package com.example.demo.repositories;

import com.example.demo.entities.courses.Course;
import com.example.demo.entities.student.CourseRegistration;
import com.example.demo.entities.student.Student;
import com.example.demo.model.GradeDto;
import com.example.demo.model.StudentGradesDto;
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

    @Query("SELECT new com.example.demo.model.GradeDto (c.student.id, c.student.name, AVG(c.grade)) FROM CourseRegistration c WHERE c.grade IS NOT NULL GROUP BY c.student.id,c.student.name")
    List<GradeDto> getAverageGrades();

    @Query("SELECT new com.example.demo.model.GradeDto (c.student.id, c.student.name, AVG(coalesce( c.grade, 0))) FROM CourseRegistration c GROUP BY c.student.id,c.student.name")
    List<GradeDto> getAverageGradesWithNull();

    @Query("SELECT new com.example.demo.model.StudentGradesDto(c.name, cr.grade) FROM CourseRegistration cr JOIN cr.course c WHERE cr.student.id = :studentId")
    List<StudentGradesDto> findStudentGradesForCourses(@Param("studentId") Long studentId);


}