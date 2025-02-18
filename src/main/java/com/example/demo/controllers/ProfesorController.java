package com.example.demo.controllers;

import com.example.demo.model.CourseDto;
import com.example.demo.model.ProfesorDto;
import com.example.demo.repositories.CourseRegistrationRepository;
import com.example.demo.services.ProfesorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("profesors/")
public class ProfesorController {

    private final ProfesorService profesorService;
    private final CourseRegistrationRepository courseRegistrationRepository;

    public ProfesorController(ProfesorService profesorService, CourseRegistrationRepository courseRegistrationRepository) {
        this.profesorService = profesorService;
        this.courseRegistrationRepository = courseRegistrationRepository;
    }


    //add new professor
    @PostMapping("/new")
    public ProfesorDto addNewProfessor(@RequestBody ProfesorDto profesorDto) {
        return profesorService.addNewProfessor(profesorDto);


    }

    //get all professors
    @GetMapping(path = "all")
    public Page<ProfesorDto> getAllProfessor(Pageable pageable) {
        return profesorService.getAllProfessors(pageable);
    }

    //professor by id
    @GetMapping(path = "{profesorId}")
    public ProfesorDto getProfessor(@PathVariable Integer profesorId) {
        return profesorService.getProfessorById(profesorId);

    }

    //delete profesor
    @DeleteMapping(path = "{profesorId}")
    public void deleteProfessor(@PathVariable Integer profesorId) {
        profesorService.deleteProfessor(profesorId);
    }

    //update profesor
    @PutMapping(path = "{profesorId}")
    public ProfesorDto updateProfessor(@PathVariable("profesorId") Integer profesorId, @RequestBody ProfesorDto profesorDto) {
        return profesorService.updateProfessor(profesorId, profesorDto);
    }

    //assign profesor to course
    @PostMapping("{courseId}/{profesorId}")
    public void assignProfessorToCourse(@PathVariable Integer courseId, @PathVariable Integer profesorId) {
        profesorService.assignProfesorToCourse(courseId, profesorId);
    }

    @GetMapping("/{profesorId}/courses")
    public Page<CourseDto> getAllProfesorCourses(@PathVariable Integer profesorId, Pageable pageable) {
        return profesorService.getAllProfesorCourses(profesorId, pageable);

    }


}
