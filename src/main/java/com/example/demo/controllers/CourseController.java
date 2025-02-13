package com.example.demo.controllers;

import com.example.demo.entities.courses.Course;
import com.example.demo.entities.student.CourseRegistration;
import com.example.demo.entities.student.Student;
import com.example.demo.model.CourseDto;
import com.example.demo.repositories.CourseRegistrationRepository;
import com.example.demo.repositories.CourseRepository;
import com.example.demo.repositories.StudentRepository;
import com.example.demo.services.CourseService;
import com.example.demo.services.StudentService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@Getter
@Setter
@RequestMapping(path = "/api/v1/student/subjects")
public class CourseController {

    private final CourseService courseService;
    private final StudentService studentService;
    private final CourseRegistrationRepository courseRegistrationRepository;
    private final CourseRepository courseRepository;

    @Autowired
    public CourseController(CourseService courseService, StudentRepository studentRepository, StudentService studentService, CourseRegistrationRepository courseRegistrationRepository, CourseRepository courseRepository) {
        this.courseService = courseService;
        this.studentService = studentService;
        this.courseRegistrationRepository = courseRegistrationRepository;
        this.courseRepository = courseRepository;
    }


    @PostMapping(path = "/{studentId}/{courseId}")
    public void assignStudentToCourse(@PathVariable("studentId") Long courseId, @PathVariable("courseId") Long studentId) {
        Student student = studentService.findById(studentId);
        Course course = courseService.findById(courseId);


        courseService.assignStudentToCourse(student, course);
    }

    @GetMapping
    public Page<CourseDto> getAllCourses(Pageable pagable) {
        return courseService.getAllCourses(pagable);
    }


    @PostMapping("/{studentId}/{courseId}/grade")
    public void addGradeToCourse(@PathVariable("studentId") Long studentId, @PathVariable("courseId") Long courseId, @RequestBody int grade) {
        Student student = studentService.findById(studentId);
        Course course = courseService.findById(courseId);

        CourseRegistration courseRegistration = courseRegistrationRepository.findByStudentAndCourse(student, course);
        courseRegistration.setGrade(grade);

        courseRegistrationRepository.save(courseRegistration);

    }

    @PutMapping(path = "courseId")
    public void updateCourse(@PathVariable("courseId") Long courseId, @RequestBody CourseDto courseDto) {
        courseService.updateCourse(courseId, courseDto);
    }

    @PostMapping("/new")
    public void addSubject(@RequestBody CourseDto courseDto) {
        courseService.addNewSubject(courseDto);
    }

    public record CourseRegistrationDto(String studentName, String courseName) {

    }

    @GetMapping("/all")
    public List<CourseRegistrationDto> getAllSubjects() {
        List<CourseRegistration> allSubjects = courseService.getAllSubjects();
        var list = new ArrayList<CourseRegistrationDto>();
        for (CourseRegistration courseRegistration : allSubjects) {
            list.add(mapToDTO(courseRegistration));
        }
        return list;

    }

    CourseRegistrationDto mapToDTO(CourseRegistration courseRegistration) {
        return new CourseRegistrationDto(courseRegistration.getStudent().getName(), courseRegistration.getCourse().getName());
    }


}
