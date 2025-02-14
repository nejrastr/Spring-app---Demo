package com.example.demo.services;

import com.example.demo.entities.student.Student;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFound;
import com.example.demo.model.StudentDto;
import com.example.demo.model.StudentGradesDto;
import com.example.demo.model.StudentMapper;
import com.example.demo.repositories.CourseRegistrationRepository;
import com.example.demo.repositories.CourseRepository;
import com.example.demo.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final StudentMapper studentMapper;
    private final CourseRegistrationRepository courseRegistrationRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository, CourseRepository courseRepository, StudentMapper studentMapper, CourseRegistrationRepository courseRegistrationRepository) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.studentMapper = studentMapper;
        this.courseRegistrationRepository = courseRegistrationRepository;
    }

    StudentDto mapToStudentDto(Student student) {
        return new StudentDto(student.getId(), student.getName(), student.getYearOfStudy(), student.getAge(), student.getEmail(), student.getDateOfBirth());
    }

    public Page<StudentDto> getStudents(Pageable pageable) {

        return studentRepository.findAll(pageable).map(this::mapToStudentDto);

    }

    public StudentDto addNewStudent(StudentDto student) {

        Optional<Student> studentOptional = studentRepository.findByEmail(student.getEmail());
        if (student.getAge() <= 19) {
            throw new BadRequestException("Age must be greater than 19");
        }
        if (student.getDateOfBirth().isAfter(LocalDate.now())) {
            throw new BadRequestException("Date of birth must be before now");

        }
        if (studentOptional.isPresent()) {
            throw new IllegalStateException("This email is taken");
        }
        Student s = mapToStudentEntity(student);
        studentRepository.save(s);
        return mapToStudentDto(s);

    }

    private Student mapToStudentEntity(StudentDto student) {
        return studentMapper.updateStudentEntity(new Student(), student);
    }


    public void deleteStudent(Long studentId) {
        boolean exists = studentRepository.existsById(studentId);
        if (!exists) {
            throw new IllegalStateException("This student does not exist");
        }
        studentRepository.deleteById(studentId);

    }


    public void updateStudent(Long studentId, StudentDto studentDto) {
        Student s = studentRepository.findById(studentId).orElseThrow(() -> new ResourceNotFound("Student not found"));

        Student updatedStudent = studentMapper.updateStudentEntity(s, studentDto);
        studentRepository.save(updatedStudent);


    }


    public Student findById(Long studentId) {

        return studentRepository.findById(studentId).orElseThrow(() -> new ResourceNotFound("Student not found"));
    }


    public StudentDto findStudentById(Long studentId) {
        Student student = studentRepository.findById(studentId).orElseThrow(() -> new ResourceNotFound("Student not found"));

        return mapToStudentDto(student);
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public List<StudentGradesDto> getAllStudentGrades(Long studentId) {
        List<StudentGradesDto> studentGrades = courseRegistrationRepository.findStudentGradesForCourses(studentId);
        return studentGrades;

    }

    public List<String> getsStudentNames(Long courseId, Integer grade) {

        return studentRepository.findByCourseIdAndGrade(courseId, grade);

    }
}
