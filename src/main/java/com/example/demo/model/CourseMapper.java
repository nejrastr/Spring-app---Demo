package com.example.demo.model;

import com.example.demo.entities.courses.Course;
import org.springframework.stereotype.Component;

@Component
public class CourseMapper {
    public CourseDto mapToCourseDto(Course course) {
        return new CourseDto(course.getId(), course.getName());
    }

    public Course updateCourseEntity(Course course, CourseDto courseDto) {
        course.setName(courseDto.getName());
        return course;
    }
}
