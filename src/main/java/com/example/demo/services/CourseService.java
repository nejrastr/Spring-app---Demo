package com.example.demo.services;

import com.example.demo.controllers.CourseController;
import com.example.demo.entities.courses.Course;
import com.example.demo.entities.student.CourseRegistration;
import com.example.demo.entities.student.Student;
import com.example.demo.event.StudentRegistrationEvent;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFound;
import com.example.demo.model.*;
import com.example.demo.repositories.CourseRegistrationRepository;
import com.example.demo.repositories.CourseRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final CourseRegistrationRepository courseRegistrationRepository;
    private final StudentService studentService;
    private final CourseMapper courseMapper;
    private final StudentMapper studentMapper;
    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    public CourseService(CourseRepository courseRepository, CourseRegistrationRepository courseRegistrationRepository, StudentService studentService, CourseMapper courseMapper, StudentMapper studentMapper, ApplicationEventPublisher eventPublisher) {
        this.courseRepository = courseRepository;
        this.courseRegistrationRepository = courseRegistrationRepository;
        this.studentService = studentService;
        this.courseMapper = courseMapper;
        this.studentMapper = studentMapper;
        this.eventPublisher = eventPublisher;
    }

    private CourseDto mapToCourseDto(Course course) {
        return new CourseDto(course.getId(), course.getName());
    }

    public Course updateCourseEntity(Course course, CourseDto courseDto) {
        try {
            course.setName(courseDto.getName());
        } catch (BadRequestException e) {
            log.error(e.getMessage());
        }
        return course;
    }

    public void updateCourse(Long courseId, CourseDto courseDto) {
        try {
            Course course = courseRepository.findById(courseId).orElseThrow(() -> new ResourceNotFound("Course not found"));
            Course updatedCourse = updateCourseEntity(course, courseDto);
            courseRepository.save(updatedCourse);
        } catch (ResourceNotFound e) {
            log.error(e.getMessage());
        }

    }


    private Course mapToCourseEntity(CourseDto courseDto) {
        return new Course(courseDto.getId(), courseDto.getName());
    }

    public CourseDto addNewSubject(CourseDto courseDto) {

        if (courseDto.getName() == null || courseDto.getName().isEmpty()) {
            throw new BadRequestException("Course name is required");
        } else if (!courseDto.getName().matches("^[A-Za-z0-9 ]+$")) {
            throw new BadRequestException("Course name contains invalid characters");
        }
        Course course = mapToCourseEntity(courseDto);
        try {

            courseRepository.save(course);


        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return mapToCourseDto(course);
    }

    @EventListener
    public void sendMessageAfterRegistration(StudentRegistrationEvent event) {
        System.out.println("Student: " + event.studentName() + " is registered" + " to the course " + event.CourseName());
    }

    @Transactional
    public void assignStudentToCourse(Student student, Course course) {
        CourseRegistration courseRegistration = new CourseRegistration();
        try {
            courseRegistration.setCourse(course);
            courseRegistration.setStudent(student);

            courseRegistrationRepository.save(courseRegistration);
            eventPublisher.publishEvent(new StudentRegistrationEvent(student.getName(), course.getName()));
        } catch (Exception e) {
            log.error(e.getMessage());
        }


    }

    public Course findById(Long courseId) {
        return courseRepository.findById(courseId).orElseThrow(() -> new ResourceNotFound("Course not found"));
    }

    public Page<CourseRegistration> getAllCourseRegistrations(Pageable pagable) {

        return courseRegistrationRepository.findAll(pagable);
    }

    public Page<CourseDto> getAllCourses(Pageable pageable) {
        return courseRepository.findAll(pageable).map(this::mapToCourseDto);
    }


    public Page<StudentDto> findStudentByCourseId(Long courseId, Pageable pagable) {
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new ResourceNotFound("Course not found"));
        Page<Student> students = courseRegistrationRepository.findStudentByCourse(course, pagable);
        if (students.isEmpty()) {
            throw new ResourceNotFound("No students enrolled to this course");
        }
        return students.map(studentMapper::mapToStudentDto);


    }


    public GradeDto calculateAverageStudenGrade(Long studentId) {
        Student student = new Student();
        try {
            student = studentService.findById(studentId);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
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

    public Page<StudentDto> getAllStudentFromCourseWithGrade(Long courseId, double grade, Pageable pageable) {
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new ResourceNotFound("Course not found"));
        Page<Student> students = courseRegistrationRepository.findStudentsByGradeAndCourse(course, grade, pageable);
        if (students.isEmpty()) {
            throw new ResourceNotFound("No students with this grade found");
        }
        return students.map(studentService::mapToStudentDto);
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

    public Page<CourseDto> getAllSubjectsByStudentOrProfessor(Long studentId, Long professorId, Pageable pageable) {
        Page<Course> courses = courseRepository.findCourseByStudentIdAndProfessorId(studentId, professorId, pageable);

        return courses.map(this::mapToCourseDto);
    }

    public void updateStudentGrade(Long studentId, Long courseId, double grade) {
        CourseRegistration cr = courseRegistrationRepository.findByStudentAndCourse(studentService.findById(studentId), findById(courseId));
        cr.setGrade(Double.valueOf(grade).intValue());
        courseRegistrationRepository.save(cr);
    }

    public Page<NumberOfCourseRegistrationsDto> getNumbersOfAllCourseRegistartions(Pageable pageable) {

        return courseRegistrationRepository.getNumberOfRegistrationsForEachCourse(pageable);
    }

    public Page<CourseController.CourseRegistrationDto> findAllCourseRegistrations(Pageable pageable) {
        Page<CourseRegistration> allSubjects = getAllCourseRegistrations(pageable);


        return allSubjects.map(this::mapToDTO);
    }

    CourseController.CourseRegistrationDto mapToDTO(CourseRegistration courseRegistration) {
        return new CourseController.CourseRegistrationDto(courseRegistration.getStudent().getName(), courseRegistration.getCourse().getName());
    }
}
