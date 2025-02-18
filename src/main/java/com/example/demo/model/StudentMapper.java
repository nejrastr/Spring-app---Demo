package com.example.demo.model;

import com.example.demo.entities.student.Student;
import org.springframework.stereotype.Component;

@Component
public class StudentMapper {

    public Student mapToStudentEntity(StudentDto student) {
        return updateStudentEntity(new Student(), student);
    }

    public Student updateStudentEntity(Student studentEntity, StudentDto student) {
        studentEntity.setId(student.getId());
        studentEntity.setName(student.getName());
        studentEntity.setYearOfStudy(student.getYearOfStudy());
        studentEntity.setDepartment(student.getDepartment());
        studentEntity.setGender(student.getGender());
        studentEntity.setAge(student.getAge());
        studentEntity.setEmail(student.getEmail());
        studentEntity.setDateOfBirth(student.getDateOfBirth());
        return studentEntity;

    }

    public StudentDto mapToStudentDto(Student student) {
        return new StudentDto(student.getId(), student.getName(), student.getYearOfStudy(), student.getDepartment(), student.getGender(), student.getAge(), student.getEmail(), student.getDateOfBirth());
    }
}
