package com.example.demo.services;

import com.example.demo.entities.student.Student;
import com.example.demo.model.DepartmentEnum;
import com.example.demo.model.GenderEnum;
import com.example.demo.repositories.StudentRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

import static org.hibernate.validator.internal.util.Contracts.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StudentServiceTest {

    @Autowired
    private StudentRepository studentRepository;
    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

    }

    //validation tests
    @Test
    void testValidStudentService() {
        Student student = new Student();
        student.setId(2L);
        student.setName("Omar");
        student.setAge(24);
        student.setEmail("omar@gmail.com");
        student.setDateOfBirth(LocalDate.of(2021, 4, 19));
        student.setYearOfStudy(1);
        student.setDepartment(DepartmentEnum.COMPUTER_SCIENCE);
        student.setGender(GenderEnum.MALE);

        Set<ConstraintViolation<Student>> violations = validator.validate(student);
        assertTrue(violations.isEmpty(), "All fields must be valid");

    }

    @Test
    void testInvalidEmail() {
        Student student = new Student();
        student.setId(2L);
        student.setName("Omar");
        student.setAge(24);
        student.setEmail("invalid");
        student.setDateOfBirth(LocalDate.of(2021, 4, 19));
        student.setYearOfStudy(1);
        student.setDepartment(DepartmentEnum.COMPUTER_SCIENCE);
        student.setGender(GenderEnum.MALE);

        Set<ConstraintViolation<Student>> violations = validator.validate(student);
        assertTrue(violations.isEmpty(), "Emaiil field is not valid");
    }

    @Test
    void testSaveAndRetrieveStudent() {
        Student student = new Student();
        student.setName("Omar");
        student.setAge(24);
        student.setEmail("omar@gmail.com");
        student.setDateOfBirth(LocalDate.of(2021, 4, 19));
        student.setYearOfStudy(1);
        student.setDepartment(DepartmentEnum.COMPUTER_SCIENCE);
        student.setGender(GenderEnum.MALE);
        studentRepository.save(student);
        Optional<Student> studentOptional = studentRepository.findById(student.getId());
        assertTrue(studentOptional.isPresent(), "Student not found");
    }

    @Test
    void testInvalidStudentSaveAndRetrive() {
        Student student = new Student();
        student.setName("Nedim");
        student.setAge(24);
        student.setEmail("omar@gmail.com");

        student.setDateOfBirth(LocalDate.of(2021, 4, 19));
        student.setYearOfStudy(1);
        student.setDepartment(DepartmentEnum.COMPUTER_SCIENCE);
        student.setGender(GenderEnum.MALE);
        studentRepository.save(student);
        Optional<Student> studentOptional = studentRepository.findByName(student.getName());
        assertFalse(studentOptional.isPresent(), "Student not found");

    }

    @Test
    void testEmptyStudentSaveAndRetrive() {
        Student student = new Student();
        student.setName("gujkgku");
        studentRepository.save(student);
        Optional<Student> studentOptional = studentRepository.findByName(student.getName());
        assertTrue(studentOptional.isPresent(), "Student not found");

    }

}
