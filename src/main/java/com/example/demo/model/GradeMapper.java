package com.example.demo.model;

import com.example.demo.entities.student.CourseRegistration;
import org.springframework.stereotype.Component;

@Component
public class GradeMapper {
    public GradeDto mapToGradeDto(CourseRegistration courseRegistration) {
        return new GradeDto(courseRegistration.getStudent().getId(), courseRegistration.getCourse().getName(), courseRegistration.getGrade());

    }


}
