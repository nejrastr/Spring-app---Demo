package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
@Getter

public enum GenderEnum {
    MALE("Male"),
    FEMALE("Female"),
    ;

    private final String code;

    @JsonValue
    public String getCode() {

        return code;
    }


    @JsonCreator
    public static GenderEnum fromCode(String code) {
        return Arrays.stream(GenderEnum.values())
                .filter(e -> e.name().equalsIgnoreCase(code) || e.getCode().equalsIgnoreCase(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid Gender: " + code));
    }
}
