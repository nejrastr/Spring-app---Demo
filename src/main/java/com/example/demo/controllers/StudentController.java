package com.example.demo.controllers;

import com.example.demo.entities.student.CourseRegistration;
import com.example.demo.model.StudentDto;
import com.example.demo.repositories.CourseRegistrationRepository;
import com.example.demo.repositories.StudentRepository;
import com.example.demo.services.CourseService;
import com.example.demo.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping(path = "api/v1/student")
public class StudentController {

    private final StudentService studentService;
    private final StudentRepository studentRepository;
    private final CourseRegistrationRepository courseRegistrationRepository;
    private final CourseService courseService;


    @Autowired

    public StudentController(StudentService studentService, StudentRepository studentRepository, CourseRegistrationRepository courseRegistrationRepository, CourseService courseService) {
        this.studentService = studentService;
        this.studentRepository = studentRepository;
        this.courseRegistrationRepository = courseRegistrationRepository;
        this.courseService = courseService;
    }


    @GetMapping
    public Page<StudentDto> getStudents(Pageable pageable) {
        return studentService.getStudents(pageable);
    }

    @GetMapping(path = "{studentId}")
    public ResponseEntity<StudentDto> getStudent(@PathVariable Long studentId) {
        StudentDto studentById = studentService.findStudentById(studentId);
        return new ResponseEntity<>(studentById, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<StudentDto> addStudent(@RequestBody StudentDto student) {
        StudentDto newStudent = studentService.addNewStudent(student);
        return new ResponseEntity<>(newStudent, HttpStatus.CREATED);

    }

    @DeleteMapping(path = "{studentId}") //path variable
    public void deleteStudent(@PathVariable("studentId") Long studentId) {
        studentService.deleteStudent(studentId);
    }

    @PutMapping(path = "{studentId}")
    public void updateStudent(@PathVariable("studentId") Long studentId, @RequestBody StudentDto student) {
        studentService.updateStudent(studentId, student);
    }


    @GetMapping(path = "{studentId}/registrations")
    public Set<CourseRegistration> getStudentCourseRegistrations(@PathVariable("studentId") Long studentId) {
        return courseRegistrationRepository.findByStudentId(studentId);
    }

    @GetMapping(path = "{courseId}/students")
    public Set<StudentDto> getCourseStudents(@PathVariable("courseId") Long courseId) {
        return courseService.findStudentByCourseId(courseId);
    }

    @GetMapping(path = "{courseId}/grade")
    public Set<StudentDto> getAllStudentsInCourseByGrade(@PathVariable Long courseId, @RequestBody int grade) {
        return courseService.getAllStudentFromCourseWithGrade(courseId, grade);
    }

}
