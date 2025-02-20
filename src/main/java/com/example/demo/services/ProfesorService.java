package com.example.demo.services;

import com.example.demo.entities.Profesor;
import com.example.demo.entities.courses.Course;
import com.example.demo.entities.student.CourseRegistration;
import com.example.demo.event.ProfessorRegistrationEvent;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFound;
import com.example.demo.model.CourseDto;
import com.example.demo.model.CourseMapper;
import com.example.demo.model.ProfesorDto;
import com.example.demo.model.ProfessorMapper;
import com.example.demo.repositories.CourseRegistrationRepository;
import com.example.demo.repositories.ProfesorRepository;
import jakarta.transaction.Transactional;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

@Service
public class ProfesorService {

    private final ProfesorRepository profesorRepository;
    private final ProfessorMapper professorMapper;
    private final CourseRegistrationRepository courseRegistrationRepository;
    private final CourseService courseService;
    private final CourseMapper courseMapper;
    private final ApplicationEventPublisher eventPublisher;

    public ProfesorService(ProfesorRepository profesorRepository, ProfessorMapper professorMapper, CourseRegistrationRepository courseRegistrationRepository, CourseService courseService, CourseMapper courseMapper, ApplicationEventPublisher eventPublisher) {
        this.profesorRepository = profesorRepository;
        this.professorMapper = professorMapper;
        this.courseRegistrationRepository = courseRegistrationRepository;
        this.courseService = courseService;
        this.courseMapper = courseMapper;
        this.eventPublisher = eventPublisher;
    }

    public ProfesorDto addNewProfessor(ProfesorDto profesorDto) {

        Profesor profesor = professorMapper.mapToProfessorEntity(profesorDto);
        profesorRepository.save(profesor);
        return professorMapper.mapToProfessorDto(profesor);


    }

    public Page<ProfesorDto> getAllProfessors(Pageable pageable) {
        Page<Profesor> professors = profesorRepository.findAll(pageable);

        return professors.map(professorMapper::mapToProfessorDto);
    }

    public ProfesorDto getProfessorById(Integer professorId) {
        ProfesorDto professor = profesorRepository.getProfesorsById(professorId);
        if (professor == null) {
            throw new BadRequestException("No professor");
        }

        return profesorRepository.getProfesorsById(professorId);
    }

    public void deleteProfessor(Integer professorId) {
        ProfesorDto professor = profesorRepository.getProfesorsById(professorId);
        if (professor == null) {
            throw new BadRequestException("No professor");
        }

        profesorRepository.deleteById(Long.valueOf(professorId));
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

        profesorRepository.save(professorMapper.mapToProfessorEntity(updatedProfessor));
        return updatedProfessor;

    }

    @EventListener

    public void writeProfessorName(ProfessorRegistrationEvent professorRegistrationEvent) {
        System.out.println(professorRegistrationEvent.name());

    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)

    public void writeProfessorNameTwice(ProfessorRegistrationEvent professorRegistrationEvent) {
        System.out.println(professorRegistrationEvent.name());
        System.out.println(professorRegistrationEvent.name());

    }

    @Transactional
    public void assignProfesorToCourse(Integer courseId, Integer profesorId) {

        List<CourseRegistration> profesorCourse = courseRegistrationRepository.findByCourseId(Long.valueOf(courseId));
        if (profesorCourse == null) {
            throw new BadRequestException("No registrations for course");
        }
        Profesor profesor = profesorRepository.findById(Long.valueOf(profesorId)).orElseThrow(() -> new ResourceNotFound("Professor not found."));
        for (CourseRegistration courseRegistration : profesorCourse) {
            courseRegistration.setProfesor(profesor);
        }
        courseRegistrationRepository.saveAll(profesorCourse);


        eventPublisher.publishEvent(new ProfessorRegistrationEvent(profesor.getName()));
    }

    public Page<CourseDto> getAllProfesorCourses(Integer profesorId, Pageable pageable) {
        Page<Course> profesorCourses = profesorRepository.getAllProfesorCoursesById(profesorId, pageable);
        return profesorCourses.map(courseMapper::mapToCourseDto);
    }
}
