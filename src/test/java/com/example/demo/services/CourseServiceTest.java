package com.example.demo.services;

import com.example.demo.config.PostgresqlContainer;
import com.example.demo.entities.student.Student;
import com.example.demo.repositories.StudentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CourseServiceTest extends PostgresqlContainer {

    @Autowired
    private StudentRepository studentRepository;

    @Test
    void testSaveAndRetrieveStudent() {
        Student student = new Student();
        student.setName("John Doe");
        student.setEmail("john.doe@gmail.com");
        studentRepository.save(student);

        Optional<Student> found = studentRepository.findById(student.getId());
        assertTrue(found.isPresent(), "Student not found");
        assertEquals("John Doe", found.get().getName());
    }


}