package com.example.demo.services;

import com.example.demo.entities.courses.Course;
import com.example.demo.entities.student.CourseRegistration;
import com.example.demo.entities.student.Student;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFound;
import com.example.demo.model.CourseDto;
import com.example.demo.model.GradeDto;
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
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new ResourceNotFound("Course not found"));
        if (courseDto.getName().isEmpty()) {
            throw new BadRequestException("Course name cannot be empty");
        }
        Course updatedCourse = updateCourseEntity(course, courseDto);
        courseRepository.save(updatedCourse);
    }


    private Course mapToCourseEntity(CourseDto courseDto) {
        return new Course(courseDto.getId(), courseDto.getName());
    }

    public CourseDto addNewSubject(CourseDto courseDto) {
        System.out.println(courseDto.getName());
        if (courseDto.getName() == null || courseDto.getName().isEmpty()) {
            throw new BadRequestException("Course name is required");
        } else if (!courseDto.getName().matches("^[A-Za-z0-9 ]+$")) {
            throw new BadRequestException("Course name contains invalid characters");
        }
        Course course = mapToCourseEntity(courseDto);
        courseRepository.save(course);
        return mapToCourseDto(course);


    }

    public void assignStudentToCourse(Student student, Course course) {
        CourseRegistration courseRegistration = new CourseRegistration();

        courseRegistration.setCourse(course);
        courseRegistration.setStudent(student);
        courseRegistrationRepository.save(courseRegistration);
    }

    public Course findById(Long courseId) {
        return courseRepository.findById(courseId).orElseThrow(() -> new ResourceNotFound("Course not found"));
    }

    public List<CourseRegistration> getAllSubjects() {
        List<CourseRegistration> courseRegistrations = courseRegistrationRepository.findAll();
        if (courseRegistrations.isEmpty()) {
            throw new ResourceNotFound("No courses found");
        }
        return courseRegistrationRepository.findAll();
    }

    public Page<CourseDto> getAllCourses(Pageable pageable) {
        return courseRepository.findAll(pageable).map(this::mapToCourseDto);
    }


    public Set<StudentDto> findStudentByCourseId(Long courseId) {
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new ResourceNotFound("Course not found"));
        Set<Student> students = courseRegistrationRepository.findStudentByCourse(course);
        if (students.isEmpty()) {
            throw new ResourceNotFound("No students enrolled to this course");
        }
        return students.stream().map(studentService::mapToStudentDto).collect(Collectors.toSet());


    }


    public GradeDto calculateAverageStudenGrade(Long studentId) {
        Student student = studentService.findById(studentId);
        return calculateAverageStudenGrade(student);
    }

    public GradeDto calculateAverageStudenGrade(Student student) {

        List<Integer> grades = courseRegistrationRepository.getGradesByStudentId(student.getId());
        int sum = 0;
        for (Integer grade : grades) {
            if (grade != null) {
                sum += grade;
            }

        }
        double average = (double) sum / grades.size();
        return new GradeDto(student.getId(), student.getName(), average);
    }

    public Set<StudentDto> getAllStudentFromCourseWithGrade(Long courseId, double grade) {
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new ResourceNotFound("Course not found"));
        Set<Student> students = courseRegistrationRepository.findStudentsByGradeAndCourse(course, grade);
        if (students.isEmpty()) {
            throw new ResourceNotFound("No students with this grade found");
        }
        return students.stream().map(studentService::mapToStudentDto).collect(Collectors.toSet());
    }

    public List<GradeDto> findAllStudentsAverages() {
        return courseRegistrationRepository.getAverageGradesWithNull();
//
    }

    public void setStudentGrade(Long studentId, Long courseId, double grade) {
        if (grade < 6) {
            throw new BadRequestException("Grade must be at least 6");
        } else if (grade > 10) {
            throw new BadRequestException("Grade must be at most 10");

        }
        Student student = studentService.findById(studentId);
        Course course = findById(courseId);
        CourseRegistration courseRegistration = courseRegistrationRepository.findByStudentAndCourse(student, course);
        courseRegistration.setGrade(Double.valueOf(grade).intValue());

        courseRegistrationRepository.save(courseRegistration);
    }
}
