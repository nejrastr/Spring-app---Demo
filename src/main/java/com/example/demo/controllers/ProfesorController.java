package com.example.demo.controllers;

import com.example.demo.model.CourseDto;
import com.example.demo.model.ProfesorDto;
import com.example.demo.repositories.CourseRegistrationRepository;
import com.example.demo.services.ProfesorService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public List<ProfesorDto> getAllProfessor() {
        return profesorService.getAllProffesors();
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
    public List<CourseDto> getAllProfesorCourses(@PathVariable Integer profesorId) {
        return profesorService.getAllProfesorCourses(profesorId);

    }


}
