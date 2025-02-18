package com.example.demo.controllers;

import com.example.demo.entities.student.CourseRegistration;
import com.example.demo.model.GradeDto;
import com.example.demo.model.StudentDto;
import com.example.demo.repositories.CourseRegistrationRepository;
import com.example.demo.repositories.StudentRepository;
import com.example.demo.services.CourseService;
import com.example.demo.services.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

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
    public ResponseEntity<StudentDto> addStudent(@Valid @RequestBody StudentDto student) {
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
    public Page<CourseRegistration> getStudentCourseRegistrations(@PathVariable("studentId") Long studentId, Pageable pageable) {
        return courseRegistrationRepository.findByStudentId(studentId, pageable);
    }

    @GetMapping(path = "{courseId}/students")
    public Page<StudentDto> getCourseStudents(@PathVariable("courseId") Long courseId, Pageable pageable) {
        return courseService.findStudentByCourseId(courseId, pageable);
    }

    @GetMapping(path = "{courseId}/grade")
    public Page<StudentDto> getAllStudentsInCourseByGrade(@PathVariable Long courseId, @RequestBody GradeDto gradeDto, Pageable pageable) {
        return courseService.getAllStudentFromCourseWithGrade(courseId, gradeDto.getGrade(), pageable);
    }

    @GetMapping(path = "{studentId}/averageGrade")
    public GradeDto getAverageGrade(@PathVariable Long studentId) {
        return courseService.calculateAverageStudenGrade(studentId);
    }

    @GetMapping(path = "all/averageGrades")
    public GradeDto getAllStudentsAverageGrades() {
        return (GradeDto) courseService.findAllStudentsAverages();

    }

    @GetMapping("/students")
    public Page<StudentDto> getStudentsNames(@RequestParam(required = false) Long courseId,
                                             @RequestParam(required = false) Integer grade, Pageable pageable) {
        return studentService.getsStudentNames(courseId, grade, pageable);


    }

    @GetMapping("/best-students")
    public StudentDto getBestStudentsByAverageScore() {
        return studentService.getBestStudentByAverageScore();
    }

    @GetMapping("/search")
    public Page<StudentDto> searchStudents(@RequestParam(required = false) String name,
                                           @RequestParam(required = false) Integer yearOfStudy,
                                           @RequestParam(required = false) Integer age,
                                           @RequestParam(required = false) String email,
                                           @RequestParam(required = false) LocalDate dateOfBirth, Pageable pageable) {
        return studentService.findStudents(name, yearOfStudy, age, email, dateOfBirth, pageable);

    }

}
