package com.example.demo.services;

import com.example.demo.entities.student.Student;
import com.example.demo.model.StudentDto;
import com.example.demo.repositories.CourseRepository;
import com.example.demo.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository, CourseRepository courseRepository) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    StudentDto mapToStudentDto(Student student) {
        return new StudentDto(student.getId(),student.getName(),student.getYearOfStudy(),student.getAge(), student.getEmail(),student.getDateOfBirth());
    }
    public Page<StudentDto> getStudents(Pageable pageable) {

        return studentRepository.findAll(pageable).map(this::mapToStudentDto);

    }

    public StudentDto addNewStudent(StudentDto student) {

        Optional<Student> studentOptional = studentRepository.findByEmail(student.getEmail());

        if (studentOptional.isPresent()) {
            throw new IllegalStateException("This email is taken");
        }
        Student s =mapToStudentEntity(student);
        studentRepository.save(s);
        return mapToStudentDto(s);

    }

    private Student mapToStudentEntity(StudentDto student) {
        return updateStudentEntity(new Student(), student);
    }

    private Student updateStudentEntity(Student studentEntity, StudentDto student) {
        studentEntity.setId(student.getId());
        studentEntity.setName(student.getName());
        studentEntity.setYearOfStudy(student.getYearOfStudy());
        studentEntity.setAge(student.getAge());
        studentEntity.setEmail(student.getEmail());
        studentEntity.setDateOfBirth(student.getDateOfBirth());
        return studentEntity;

    }


    public void deleteStudent(Long studentId) {
        boolean exists = studentRepository.existsById(studentId);
        if (!exists) {
            throw new IllegalStateException("This student does not exist");
        }
        studentRepository.deleteById(studentId);

    }



    public void updateStudent(Long studentId, StudentDto studentDto) {
        Student s=studentRepository.findById(studentId).orElse(null);

        Student updatedStudent = updateStudentEntity(s,studentDto);
        studentRepository.save(updatedStudent);



    }


    public Student findById(Long studentId) {

        return studentRepository.findById(studentId).orElseThrow(() -> new IllegalStateException("This student does not exist"));
    }


    public StudentDto findStudentById(Long studentId) {
       Optional<Student> studentOptional = studentRepository.findById(studentId);
       Student student=studentOptional.get();
       return mapToStudentDto(student);
    }
}
