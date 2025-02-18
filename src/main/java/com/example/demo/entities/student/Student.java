package com.example.demo.entities.student;

import com.example.demo.model.DepartmentEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "student", uniqueConstraints = {
        @UniqueConstraint(name = "uc_student_email", columnNames = {"email"})
})
@Getter
@Setter// TODO zasto ne treba data na enitity
@AllArgsConstructor
@NoArgsConstructor
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "student")
    @JsonIgnore
    Set<CourseRegistration> registrations;


    @NotBlank(message = "Name can not be empty")
    private String name;

    @Enumerated(EnumType.STRING)
    private DepartmentEnum department;
    private int yearOfStudy;


    private int age;


    private String email;


    private LocalDate dateOfBirth;


}
