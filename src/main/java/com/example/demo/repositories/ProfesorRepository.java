package com.example.demo.repositories;

import com.example.demo.entities.Profesor;
import com.example.demo.model.ProfesorDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProfesorRepository extends JpaRepository<Profesor, Long> {

    @Query("SELECT new com.example.demo.model.ProfesorDto(p.id,p.name,p.email)FROM Profesor p WHERE p.id=:profesorId")
    ProfesorDto getProfesorsById(@Param("profesorId") Integer id);
}
