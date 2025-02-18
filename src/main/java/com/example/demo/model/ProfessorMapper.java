package com.example.demo.model;

import com.example.demo.entities.Profesor;
import org.springframework.stereotype.Component;

@Component
public class ProfessorMapper {

    public ProfesorDto mapToProfessorDto(Profesor profesor) {
        return new ProfesorDto(profesor.getId(), profesor.getName(), profesor.getEmail());

    }

    public Profesor mapToProfessorEntity(ProfesorDto profesorDto) {
        return new Profesor(profesorDto.getId(), profesorDto.getName(), profesorDto.getEmail());
    }
}
