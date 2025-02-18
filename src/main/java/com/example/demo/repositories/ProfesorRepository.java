package com.example.demo.repositories;

import com.example.demo.entities.Profesor;
import com.example.demo.entities.courses.Course;
import com.example.demo.model.ProfesorDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProfesorRepository extends JpaRepository<Profesor, Long> {

    @Query("SELECT new com.example.demo.model.ProfesorDto(p.id,p.name,p.email)FROM Profesor p WHERE p.id=:profesorId")
    ProfesorDto getProfesorsById(@Param("profesorId") Integer id);

    @Query("SELECT c FROM Course c LEFT JOIN CourseRegistration cr ON c.id = cr.course.id WHERE cr.profesor.id=:profesorId")
    Page<Course> getAllProfesorCoursesById(@Param("profesorId") Integer profesorId, Pageable pageable);
}
