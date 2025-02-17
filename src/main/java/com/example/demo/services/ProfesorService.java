package com.example.demo.services;

import com.example.demo.entities.Profesor;
import com.example.demo.entities.courses.Course;
import com.example.demo.entities.student.CourseRegistration;
import com.example.demo.exception.BadRequestException;
import com.example.demo.model.CourseDto;
import com.example.demo.model.CourseMapper;
import com.example.demo.model.ProfesorDto;
import com.example.demo.model.ProfessorMapper;
import com.example.demo.repositories.CourseRegistrationRepository;
import com.example.demo.repositories.ProfesorRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProfesorService {

    private final ProfesorRepository profesorRepository;
    private final ProfessorMapper professorMapper;
    private final CourseRegistrationRepository courseRegistrationRepository;
    private final CourseService courseService;
    private final CourseMapper courseMapper;

    public ProfesorService(ProfesorRepository profesorRepository, ProfessorMapper professorMapper, CourseRegistrationRepository courseRegistrationRepository, CourseService courseService, CourseMapper courseMapper) {
        this.profesorRepository = profesorRepository;
        this.professorMapper = professorMapper;
        this.courseRegistrationRepository = courseRegistrationRepository;
        this.courseService = courseService;
        this.courseMapper = courseMapper;
    }

    public ProfesorDto addNewProfessor(ProfesorDto profesorDto) {

        Profesor profesor = professorMapper.mapToProfesorEntity(profesorDto);
        profesorRepository.save(profesor);
        return professorMapper.mapToProfessorDto(profesor);


    }

    public List<ProfesorDto> getAllProffesors() {
        List<Profesor> profesors = profesorRepository.findAll();
        if (profesors.isEmpty()) {
            throw new BadRequestException("No professors");

        }
        List<ProfesorDto> profesorDtos = new ArrayList<>();
        for (Profesor profesor : profesors) {
            profesorDtos.add(professorMapper.mapToProfessorDto(profesor));
        }
        return profesorDtos;
    }

    public ProfesorDto getProfessorById(Integer profesorId) {
        ProfesorDto profesor = profesorRepository.getProfesorsById(profesorId);
        if (profesor == null) {
            throw new BadRequestException("No professor");
        }

        return profesorRepository.getProfesorsById(profesorId);
    }

    public void deleteProfessor(Integer profesorId) {
        ProfesorDto profesor = profesorRepository.getProfesorsById(profesorId);
        if (profesor == null) {
            throw new BadRequestException("No professor");
        }

        profesorRepository.deleteById(Long.valueOf(profesorId));
    }

    public ProfesorDto updateProfessor(Integer profesorId, ProfesorDto profesorDto) {
        ProfesorDto updatedProfessor = profesorRepository.getProfesorsById(profesorId);
        if (updatedProfessor == null) {
            throw new BadRequestException("No professor");
        }
        if (profesorDto.getName() != null) {
            updatedProfessor.setName(profesorDto.getName());
        }
        if (profesorDto.getEmail() != null) {
            updatedProfessor.setEmail(profesorDto.getEmail());
        }

        profesorRepository.save(professorMapper.mapToProfesorEntity(updatedProfessor));
        return updatedProfessor;

    }

    public void assignProfesorToCourse(Integer courseId, Integer profesorId) {

        List<CourseRegistration> profesorCourse = courseRegistrationRepository.findByCourseId(Long.valueOf(courseId));
        if (profesorCourse == null) {
            throw new BadRequestException("No registrations for course");
        }
        Profesor profesor = profesorRepository.findById(Long.valueOf(profesorId)).orElse(null);
        for (CourseRegistration courseRegistration : profesorCourse) {
            courseRegistration.setProfesor(profesor);
        }
        courseRegistrationRepository.saveAll(profesorCourse);
    }

    public List<CourseDto> getAllProfesorCourses(Integer profesorId) {
        List<Course> profesorCourses = profesorRepository.getAllProfesorCoursesById(profesorId);
        return profesorCourses.stream().map(courseMapper::mapToCourseDto).toList();
    }
}
