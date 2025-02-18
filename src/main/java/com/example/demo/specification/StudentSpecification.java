package com.example.demo.specification;

import com.example.demo.entities.student.Student;
import com.example.demo.model.DepartmentEnum;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class StudentSpecification {

    public static Specification<Student> hasName(String name) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("name"), name);

    }

    public static Specification<Student> hasEmail(String email) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("email"), email);
    }

    public static Specification<Student> hasAge(Integer age) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("age"), age);
    }

    public static Specification<Student> hasDOB(LocalDate dateOfBirth) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("dateOfBirth"), dateOfBirth);
    }

    public static Specification<Student> hasYOB(Integer yearOfStudy) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("yearOfStudy"), yearOfStudy);
    }

    public static Specification<Student> hasDepartment(DepartmentEnum department) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("department"), department);
    }
}
