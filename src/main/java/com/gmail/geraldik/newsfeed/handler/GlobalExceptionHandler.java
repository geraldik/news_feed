package com.gmail.geraldik.newsfeed.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@ControllerAdvice
@AllArgsConstructor
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<?> handle(MethodArgumentNotValidException e) {
        log.error(e.getMessage());
        return ResponseEntity.badRequest().body(
                e.getFieldErrors().stream()
                        .map(f -> Map.of(
                                f.getField(),
                                String.format("%s. Actual value: %s", f.getDefaultMessage(), f.getRejectedValue())
                        ))
                        .collect(Collectors.toList())
        );
    }

    @ExceptionHandler({ResponseStatusException.class})
    public ResponseEntity<?> handle(ResponseStatusException e) {
        log.error(e.getMessage());
        return ResponseEntity.status(404).body(
                new HashMap<>() {
                    {
                        put("reason", e.getReason());
                        put("message", "Failed update attempt");
                    }
                }
        );
    }

        @ExceptionHandler({NoSuchElementException.class})
        public ResponseEntity<?> handle(NoSuchElementException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(404).body(
                    new HashMap<>() {
                        {
                            put("reason", "Failed getting item");
                            put("message", e.getMessage());
                        }
                    }
            );
    }
}
