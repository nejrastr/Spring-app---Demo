package com.example.demo.model;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentDto {

    private Long id;
    private String name;
    private Integer yearOfStudy;
    @Column(nullable = true)
    private DepartmentEnum department;
    private Integer age;
    private String email;
    private LocalDate dateOfBirth;
}
