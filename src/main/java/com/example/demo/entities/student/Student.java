package com.example.demo.entities.student;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "student")
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


    private String name;
    private int yearOfStudy;

    private int age;
    private String email;
    private LocalDate dateOfBirth;



}
