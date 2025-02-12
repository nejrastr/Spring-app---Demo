package com.example.demo.services;

import com.example.demo.entities.student.Student;
import com.example.demo.model.StudentDto;
import com.example.demo.model.StudentMapper;
import com.example.demo.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Function;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;
    public Function<? super Student,? extends StudentDto> mapToStudentDto;

    @Autowired
    public StudentService(StudentRepository studentRepository, StudentMapper studentMapper) {
        this.studentRepository = studentRepository;
        this.studentMapper = studentMapper;
    }

    StudentDto mapToStudentDto(Student student) {
        return new StudentDto(student.getId(), student.getName(), student.getYearOfStudy(), student.getAge(), student.getEmail(), student.getDateOfBirth());
    }

    public Page<StudentDto> getStudents(Pageable pageable) {

        return studentRepository.findAll(pageable).map(this::mapToStudentDto);

    }

    public StudentDto addNewStudent(StudentDto student) {

        Optional<Student> studentOptional = studentRepository.findByEmail(student.getEmail());

        if (studentOptional.isPresent()) {
            throw new IllegalStateException("This email is taken");
        }
        Student s = studentMapper.mapToStudentEntity(student);
        studentRepository.save(s);
        return mapToStudentDto(s);

    }


    public void deleteStudent(Long studentId) {
        boolean exists = studentRepository.existsById(studentId);
        if (!exists) {
            throw new IllegalStateException("This student does not exist");
        }
        studentRepository.deleteById(studentId);

    }


    public void updateStudent(Long studentId, StudentDto studentDto) {
        Student s = studentRepository.findById(studentId).
                orElseThrow(() -> new IllegalStateException("This student does not exist"));

        Student updatedStudent = studentMapper.updateStudentEntity(s, studentDto);
        studentRepository.save(updatedStudent);
    }


    public Student findById(Long studentId) {
        return studentRepository.findById(studentId).orElseThrow(() -> new IllegalStateException("This student does not exist"));
    }


    public StudentDto findStudentById(Long studentId) {
        Student student = studentRepository.findById(studentId).orElseThrow(() -> new RuntimeException("No student found"));
        return mapToStudentDto(student);
    }
}
