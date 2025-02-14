package com.example.demo.exception;

import java.time.LocalDateTime;

public record ErrorDto(String message, LocalDateTime timestamp) {
}
