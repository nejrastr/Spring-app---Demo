package com.example.demo.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class NumberOfCourseRegistrationsDto {
    private Long numberOfCourseRegistrations;
    private String courseName;

}
