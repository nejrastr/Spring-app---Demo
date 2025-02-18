package com.example.demo.entities;

import com.example.demo.entities.student.CourseRegistration;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "Profesor", uniqueConstraints = {
        @UniqueConstraint(name = "uc_profesor_email", columnNames = {"email"})
})
@AllArgsConstructor
@NoArgsConstructor
public class Profesor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    private String name;

    private String email;
    private

    @OneToMany(mappedBy = "profesor")
    Set<CourseRegistration> profesorSubjects;


    public Profesor(Integer id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public Profesor(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
