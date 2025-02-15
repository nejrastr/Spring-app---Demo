package com.example.demo.controllers;

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


    @PostMapping("/new")
    public ProfesorDto addNewProfessor(@RequestBody ProfesorDto profesorDto) {
        return profesorService.addNewProfessor(profesorDto);


    }

    @GetMapping(path = "all")
    public List<ProfesorDto> getAllProfessor() {
        return profesorService.getAllProffesors();
    }

    @GetMapping(path = "{profesorId}")
    public ProfesorDto getProfessor(@PathVariable Integer profesorId) {
        return profesorService.getProfessorById(profesorId);

    }

    @DeleteMapping(path = "{profesorId}")
    public void deleteProfessor(@PathVariable Integer profesorId) {
        profesorService.deleteProfessor(profesorId);
    }

    @PutMapping(path = "{profesorId}")
    public ProfesorDto updateProfessor(@PathVariable("profesorId") Integer profesorId, @RequestBody ProfesorDto profesorDto) {
        return profesorService.updateProfessor(profesorId, profesorDto);
    }

    @PostMapping("{courseId}/{profesorId}")
    public void assignProfessorToCourse(@PathVariable Integer courseId, @PathVariable Integer profesorId) {
        profesorService.assignProfesorToCourse(courseId, profesorId);
    }
}
