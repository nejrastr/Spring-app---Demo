package com.example.demo.services;

import com.example.demo.entities.courses.Course;
import com.example.demo.entities.student.CourseRegistration;
import com.example.demo.entities.student.Student;
import com.example.demo.model.CourseDto;
import com.example.demo.model.StudentDto;
import com.example.demo.repositories.CourseRegistrationRepository;
import com.example.demo.repositories.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final CourseRegistrationRepository courseRegistrationRepository;
    private final StudentService studentService;

    @Autowired
    public CourseService(CourseRepository courseRepository, CourseRegistrationRepository courseRegistrationRepository, StudentService studentService) {
        this.courseRepository = courseRepository;
        this.courseRegistrationRepository = courseRegistrationRepository;
        this.studentService = studentService;
    }

    private CourseDto mapToCourseDto(Course course) {
        return new CourseDto(course.getId(), course.getName());
    }

    public Course updateCourseEntity(Course course, CourseDto courseDto) {
        course.setName(courseDto.getName());
        return course;
    }

    public void updateCourse(Long courseId, CourseDto courseDto) {
        Course course = courseRepository.findById(courseId).orElseThrow();
        Course updatedCourse = updateCourseEntity(course, courseDto);
        courseRepository.save(updatedCourse);
    }


    private Course mapToCourseEntity(CourseDto courseDto) {
        return updateCourseEntity(new Course(), courseDto);
    }

    public void addNewSubject(CourseDto courseDto) {
        Course course = mapToCourseEntity(courseDto);
        courseRepository.save(course);

    }

    public void assignStudentToCourse(Student student, Course course) {
        CourseRegistration courseRegistration = new CourseRegistration();

        courseRegistration.setCourse(course);
        courseRegistration.setStudent(student);
        courseRegistrationRepository.save(courseRegistration);
    }

    public Course findById(Long courseId) {
        return courseRepository.findById(courseId).orElseThrow(() -> new RuntimeException("Course not found"));
    }

    public List<CourseRegistration> getAllSubjects() {
        return courseRegistrationRepository.findAll();
    }

    public Page<CourseDto> getAllCourses(Pageable pageable) {
        return courseRepository.findAll(pageable).map(this::mapToCourseDto);
    }


    public Set<StudentDto> findStudentByCourseId(Long courseId) {
        Course course = courseRepository.findById(courseId).orElseThrow();
        Set<Student> students = courseRegistrationRepository.findStudentByCourse(course);
        return students.stream().map(studentService::mapToStudentDto).collect(Collectors.toSet());


    }


    public Set<StudentDto> getAllStudentFromCourseWithGrade(Long courseId, int grade) {
        Course course = courseRepository.findById(courseId).orElseThrow();
        Set<Student> students = courseRegistrationRepository.findStudentsByGradeAndCourse(course, grade);
        return students.stream().map(studentService::mapToStudentDto).collect(Collectors.toSet());
    }
}
