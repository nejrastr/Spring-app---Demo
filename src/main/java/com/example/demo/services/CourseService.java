package com.example.demo.services;

import com.example.demo.entities.courses.Course;
import com.example.demo.entities.student.CourseRegistration;
import com.example.demo.entities.student.Student;
import com.example.demo.model.CourseDto;
import com.example.demo.model.StudentDto;
import com.example.demo.repositories.CourseRegistrationRepository;
import com.example.demo.repositories.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final CourseRegistrationRepository courseRegistrationRepository;
    private final StudentService studentService;
    private final RestTemplate myRestClient;
    private final RestTemplate myKlixClient;

    @Autowired
    public CourseService(CourseRepository courseRepository, CourseRegistrationRepository courseRegistrationRepository, @Lazy StudentService studentService, RestTemplate myRestClient, RestTemplate myKlixClient) {
        this.courseRepository = courseRepository;
        this.courseRegistrationRepository = courseRegistrationRepository;
        this.studentService = studentService;
        this.myRestClient = myRestClient;
        this.myKlixClient = myKlixClient;
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

    public Page<StudentDto> findByCourse(Long courseId, Pageable pageable) {
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new RuntimeException("noup"));
        Page<Student> students = courseRegistrationRepository.findByCourse(course, pageable);

        return students.map(studentService::mapToStudentDto);


    }


    public Page<StudentDto> findByGrade(String grade, Pageable pageable) {
        return null;
    }
}
